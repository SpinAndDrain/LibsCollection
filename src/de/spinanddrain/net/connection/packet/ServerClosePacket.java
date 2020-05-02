package de.spinanddrain.net.connection.packet;

public class ServerClosePacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914996874007594613L;

	/**
	 * Creates a new <code>ServerClosePacket</code>.
	 * 
	 */
	public ServerClosePacket() {
		super("net.server.connection.close");
	}
	
}
