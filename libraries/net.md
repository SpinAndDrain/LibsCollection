# net

**Version:** 1.0

**Author:** SpinAndDrain

**Description:** A simple Client-Server Socket tool to send packets through a network.

### The Use

Here is a simple example:

**Client**
````java
// Connecting to localhost:8123
Client client = new Client("localhost", 8123);
try {
  // Connect the client to the server
  client.connect();
  
	// Register this to reponse with pongs on pings,
	// otherwise the client will time out
	client.register("net.server.keepalive.ping", new Paramable<Packet>() {
		@Override
		public void run(Packet param) {
			try {
				client.sendPacket(new PongPacket());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	
	// Register this in the case the server closes first,
	// otherwise the client will time out here too
	client.register("net.server.connection.close", x -> {
		new Thread(() -> client.disconnect()).start();
	});
	
	// Send a packet to the server
	client.sendPacket(new Packet("de.spinanddrain.test", 29));
	
	// Close the client
	client.disconnect();
} catch (Exception e) {
	e.printStackTrace();
}
````

**Server**
````java
// Listening on port 8123
Server server = new Server(8123);

// Register this to receive the packet later
server.register("de.spinanddrain.test", new Callback<Packet, Socket>() {
	@Override
	public void respond(Packet a, Socket b) {
		for(Object o : a.toArray())
			System.out.println(o);
    // Always reply or the client will time out!
		server.reply(b, new DefaultCallbackPacket());
	}
});
````

**Output**

This should print a **29** out into the server's application console. If you want to disable the debug, you simply can
do this with ````client.debug = false; ```` and ````server.debug = false;````.

### Useful Classes
* [Client](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/net/Client.java)
* [Server](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/net/Server.java)
* [Packet](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/net/connection/packet/Packet.java)
