package de.spinanddrain.net.connection.packet;

public class PongPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 347393158588352713L;

	/**
	 * Creates a new <code>PongPacket</code>.
	 * 
	 */
	public PongPacket() {
		super("net.client.respond.pong");
	}
	
}
