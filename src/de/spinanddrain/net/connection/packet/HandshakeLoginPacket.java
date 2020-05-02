package de.spinanddrain.net.connection.packet;

public class HandshakeLoginPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598651194493163931L;

	/**
	 * Creates a new <code>HandshakeLoginPacket</code>
	 * without the client's sign.
	 * 
	 */
	public HandshakeLoginPacket() {
		super("net.client.login");
	}
	
	/**
	 * Creates a new <code>HandshakeLoginPacket</code>
	 * with the clients sign.
	 * 
	 * @param sign
	 */
	public HandshakeLoginPacket(String sign) {
		this();
		super.signature = sign;
	}

}
