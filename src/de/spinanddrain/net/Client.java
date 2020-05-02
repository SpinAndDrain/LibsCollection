package de.spinanddrain.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.spinanddrain.net.connection.Paramable;
import de.spinanddrain.net.connection.packet.CleanDisconnectingPacket;
import de.spinanddrain.net.connection.packet.HandshakeLoginPacket;
import de.spinanddrain.net.connection.packet.Packet;
import de.spinanddrain.net.exception.ConnectionClosedException;
import de.spinanddrain.net.exception.MethodAlreadyRegisteredException;
import de.spinanddrain.net.exception.UnknownConnectionException;

public class Client {
	
	private final Map<String, Paramable<Packet>> registry;
	private final InetSocketAddress address;
	private Socket socket;
	private int timeout;
	private Thread main, ping;
	private volatile boolean running;
	private long lastSeen;
	public boolean debug = true;
	private boolean disconnected;
	private long pingInterval;
	private String clientId;
	
	/**
	 * Creates a new client instance with the specified host name
	 * and port. The timeout limit gets initialized with 3000, the ping interval
	 * with 31000 and the client id with a random UUID.
	 * 
	 * @param hostname the target servers host name
	 * @param port the target servers port
	 */
	public Client(String hostname, int port) {
		this(hostname, port, 3000, 31000, UUID.randomUUID().toString());
	}
	
	/**
	 * Creates a new client instance with the specified values.
	 * 
	 * @param hostname the target servers host name
	 * @param port the target servers port
	 * @param timeout the timeout limit while sending packets
	 * @param pingInterval the interval of the ping process
	 * @param clientId the id of this client
	 */
	public Client(String hostname, int port, int timeout, long pingInterval, String clientId) {
		address = new InetSocketAddress(hostname, port);
		this.timeout = timeout;
		this.pingInterval = pingInterval;
		this.clientId = clientId;
		this.registry = new HashMap<String, Paramable<Packet>>();
	}
	
	/**
	 * Connects the client to the server.
	 * 
	 * @throws IOException if the login fails
	 */
	public void connect() throws IOException {
		debug("Connection...");
		running = true;
		login();
		lastSeen = System.currentTimeMillis();
		if(main == null) {
			main = new Thread(() -> {
				while(running) {
					while(socket.isClosed()) {}
					try {
						ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
						Object o = ois.readObject();
						lastSeen = System.currentTimeMillis();
						if(!running)
							return;
						if(!(o instanceof Packet)) {
							throw new IllegalArgumentException("Unknown packet");
						}
						Packet packet = (Packet) o;
						for(String key : registry.keySet()) {
							if(key.equals(packet.getId())) {
								new Thread(() -> {
									registry.get(key).run(packet);
								}).start();
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						if(!disconnected) {
							debug("Connection lost");
							new Thread(() -> disconnect(false)).start();
						}
					}
				}
			});
			main.start();
		}
		ping = new Thread(() -> {
			while(running) {
				if(System.currentTimeMillis() > lastSeen + pingInterval) {
					debug("Connection lost");
					new Thread(() -> disconnect(false)).start();
					break;
				}
			}
		});
		ping.start();
	}
	
	/**
	 * Disconnects this client clean from the connected
	 * server.
	 * 
	 */
	public void disconnect() {
		this.disconnect(true);
	}
	
	/**
	 * Disconnects this client from the server.
	 * 
	 * @param sayGoodbye true = clean, false = force
	 */
	private void disconnect(boolean sayGoodbye) {
		if(disconnected)
			return;
		debug("Stopping...");
		disconnected = true;
		running = false;
		unregisterAll();
		try {
			if(main.isAlive()) {
				main.interrupt();
			}
			if(socket.isConnected() || !socket.isClosed()) {
				if(sayGoodbye)
					logout();
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		socket = null;
	}
	
	/**
	 * Registers a new registry entry for the evaluation of packets.
	 * 
	 * @param key the packets identifier (id)
	 * @param task the executed task if the packet is received
	 */
	public void register(String key, Paramable<Packet> task) {
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
	 * Sends a packet to the connected server. The client
	 * will shutdown if the server stops to respond.
	 * 
	 * @param packet the packet which should be send
	 * @return the received callback packet, null if the received
	 * 			object is invalid
	 */
	public synchronized Packet sendPacket(Packet packet) {
		try {
			debug("Sending packet (" + packet.getClass().getSimpleName() + ")");
			Socket con = new Socket();
			con.connect(address, timeout);
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
			packet.signature = clientId;
			oos.writeObject(packet);
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(con.getInputStream()));
			Object o = ois.readObject();
			lastSeen = System.currentTimeMillis();
			oos.close();
			ois.close();
			con.close();
			if(o instanceof Packet)
				return (Packet) o;
		} catch(Exception e) {
			debug("Server stopped responding");
			disconnect(false);
		}
		return null;
	}
	
	/**
	 * Connects to the server and creates a login session.
	 * 
	 * @throws IOException
	 */
	private void login() throws IOException {
		if(socket != null && socket.isConnected())
			throw new UnknownConnectionException("Connection already initialized");
		socket = new Socket();
		socket.connect(address, timeout);
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		oos.writeObject(new HandshakeLoginPacket(clientId));
		oos.flush();
		debug("Successfully connected to " + address.getHostString());
	}
	
	/**
	 * Disconnects this client clean from the
	 * connected server.
	 * 
	 * @throws IOException
	 */
	private void logout() throws IOException {
		if(socket == null)
			throw new ConnectionClosedException();
		Socket con = new Socket();
		con.connect(address, timeout);
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
		oos.writeObject(new CleanDisconnectingPacket(clientId));
		oos.flush();
		oos.close();
		con.close();
	}
	
	/**
	 * Prints a debug.
	 * 
	 * @param message the debug message
	 */
	private void debug(String message) {
		if(debug)
			System.out.println("[Client] " + message);
	}
	
}
