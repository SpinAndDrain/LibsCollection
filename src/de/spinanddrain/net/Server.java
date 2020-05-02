package de.spinanddrain.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import de.spinanddrain.net.connection.Callback;
import de.spinanddrain.net.connection.packet.DefaultCallbackPacket;
import de.spinanddrain.net.connection.packet.HandshakeLoginPacket;
import de.spinanddrain.net.connection.packet.Packet;
import de.spinanddrain.net.connection.packet.PingPacket;
import de.spinanddrain.net.connection.packet.ServerClosePacket;
import de.spinanddrain.net.exception.MethodAlreadyRegisteredException;

public class Server {

	private final Map<String, Callback<Packet, Socket>> registry;
	private volatile Map<ClientConnection, Long> clients;
	private ServerSocket server;
	private volatile boolean running;
	public boolean debug = true;
	private boolean stopped;
	private int port;
	private long pingInterval;
	private Thread main, keepAlive;
	
	/**
	 * Creates a new server instance with the specified
	 * port and a ping interval of 30000 and starts
	 * the server.
	 * 
	 * @param port the servers port
	 */
	public Server(int port) {
		this(port, 30000L);
	}
	
	/**
	 * Creates a new server instance with the specified
	 * values and starts the server.
	 * 
	 * @param port the servers port
	 * @param pingInterval the interval between each ping-pong process
	 */
	public Server(int port, long pingInterval) {
		this.port = port;
		this.pingInterval = pingInterval;
		this.clients = new HashMap<ClientConnection, Long>();
		this.registry = new HashMap<String, Callback<Packet, Socket>>();
		start();
	}
	
	/**
	 * Starts the server.
	 * 
	 */
	private void start() {
		debug("Starting server...");
		running = true;
		main = new Thread(() -> {
			if(server == null) {
				try {
					server = new ServerSocket(port);
				} catch (IOException e) {
					debug("Startup failed");
					e.printStackTrace();
					running = false;
					return;
				}
			}
			while(!Thread.interrupted() && running && server != null) {
				try {
					Socket socket = server.accept();
					ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					Object o = ois.readObject();
					if(!(o instanceof Packet)) {
						throw new IllegalArgumentException("Unknown packet");
					}
					Packet packet = (Packet) o;
					debug("Packet received (" + o.getClass().getSimpleName() + ")");
					for(String reg : registry.keySet()) {
						if(packet.getId().equals(reg)) {
							new Thread(() -> {
								registry.get(reg).respond(packet, socket);
								if(!(packet instanceof HandshakeLoginPacket)) {
									try {
										socket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}).start();
							break;
						}
					}
				} catch(Exception e) {
					try {
						stop();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		keepAlive = new Thread(() -> {
			while(running) {
				try {
					Thread.sleep(pingInterval);
					for(ClientConnection con : clients.keySet()) {
						if(timeout(con)) {
							debug("Client " + con + " disconnected - Timed out");
							clients.remove(con);
						}
					}
					broadcast(new PingPacket());
				} catch (InterruptedException e) {
				}
			}
		});
		main.start();
		keepAlive.start();
		registry.put("net.client.login", (x, y) -> {
			debug("New Client login");
			if(!clients.containsKey(cast(x.signature)))
				clients.put(new ClientConnection(x.signature, y), System.currentTimeMillis());
		});
		registry.put("net.client.respond.pong", (x, y) -> {
			ClientConnection con = cast(x.signature);
			if(!timeout(con)) {
				renew(con);
				reply(y, new DefaultCallbackPacket());
				debug("Client responded with pong.");
			} else {
				try {
					con.socket.close();
					clients.remove(con);
					debug("Client " + con + " disconnected - Timed out");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		registry.put("net.client.connection.disconnect.clean", (x, y) -> {
			ClientConnection con = cast(x.signature);
			debug("Client " + con + " disconnected cleanly");
			try {
				if(con.socket != null)
					con.socket.close();
			} catch (IOException e) {
			}
			clients.remove(con);
		});
	}

	/**
	 * Renews a clients current timeout value.
	 * 
	 * @param s
	 */
	private void renew(ClientConnection s) {
		if(clients.containsKey(s))
			clients.remove(s);
		clients.put(s, System.currentTimeMillis());
	}
	
	/**
	 * Checks if a client responded in time.
	 * 
	 * @param s the client
	 * @return false if it responded in time, true if it timed out
	 */
	private boolean timeout(ClientConnection s) {
		return System.currentTimeMillis() > (clients.get(s) + pingInterval + 3000);
	}
	
	/**
	 * Registers a new registry entry for the evaluation of packets.
	 * 
	 * @param key the packets identifier (id)
	 * @param task the executed task if the packet is received
	 */
	public void register(String key, Callback<Packet, Socket> task) {
		if(registry.containsKey(key))
			throw new MethodAlreadyRegisteredException();
		registry.put(key, task);
	}

	/**
	 * Unregisters all registry entries.
	 * 
	 */
	public void unregisterAll() {
		registry.clear();
	}
	
	/**
	 * Sends a reply packet via a temporary socket.
	 * 
	 * @param socket the temporary socket
	 * @param packet the callback packet
	 */
	public synchronized void reply(Socket socket, Packet packet) {
		this.sendMessage(new ClientConnection(null, socket), packet);
	}
	
	/**
	 * Sends a packet to the specified receiver.
	 * 
	 * @param receiver the receiving client
	 * @param packet the packet which should be send
	 */
	public synchronized void sendMessage(ClientConnection receiver, Packet packet) {
		debug("Sending packet (" + packet.getClass().getSimpleName() + ") to " + receiver);
		if(!receiver.socket.isConnected()) {
			clients.remove(receiver);
			debug("Client " + receiver + " disconnected");
			return;
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(receiver.socket.getOutputStream()));
			oos.writeObject(packet);
			oos.flush();
		} catch (IOException e) {
			clients.remove(receiver);
			debug("Client " + receiver + " disconnected");
		}
	}
	
	/**
	 * Sends a packet to all currently connected
	 * clients.
	 * 
	 * @param packet the packet which should be send
	 */
	public synchronized void broadcast(Packet packet) {
		for(ClientConnection client : clients.keySet())
			this.sendMessage(client, packet);
	}
	
	/**
	 * Stops the server and executes a clean logout process
	 * for all connected clients.
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException {
		if(stopped)
			return;
		debug("Stopping server...");
		broadcast(new ServerClosePacket());
		stopped = true;
		running = false;
		unregisterAll();
		if(keepAlive.isAlive())
			keepAlive.interrupt();
		long a = System.currentTimeMillis();
		while(System.currentTimeMillis() < a + 5000 && openConnectionsAvailable()) {}
		if(main.isAlive())
			main.interrupt();
		clients.clear();
		if(server != null)
			server.close();
		server = null;
	}
	
	/**
	 * 
	 * @return true if any client is currently connected
	 * 			with this server
	 */
	public boolean openConnectionsAvailable() {
		for(ClientConnection con : clients.keySet()) {
			if(!con.socket.isClosed()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Prints a debug.
	 * 
	 * @param message the debug message
	 */
	private void debug(String message) {
		if(debug)
			System.out.println("[Server] " + message);
	}
	
	/**
	 * Casts the specified signature to the associated
	 * <code>ClientConnection</code>.
	 * 
	 * @param signature the client id
	 * @return the casted <code>ClientConnection</code>
	 */
	private ClientConnection cast(String signature) {
		for(ClientConnection con : clients.keySet())
			if(con.id.equals(signature))
				return con;
		return null;
	}
	
	public static class ClientConnection {
		
		private String id;
		private Socket socket;
		
		/**
		 * Creates a new <code>ClientConnection</code> instance with
		 * the specified values.
		 * 
		 * @param id the client's id
		 * @param socket the client's main socket
		 */
		private ClientConnection(String id, Socket socket) {
			this.id = id;
			this.socket = socket;
		}
		
		@Override
		public String toString() {
			return "[" + id + "@" + socket.getRemoteSocketAddress() + "]";
		}
		
	}
	
}
