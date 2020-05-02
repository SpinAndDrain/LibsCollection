package de.spinanddrain.net.connection.packet;

public class CleanDisconnectingPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4352262818595114349L;

	/**
	 * Creates a new <code>CleanDisconnectingPacket</code>
	 * without the client's sign.
	 * 
	 */
	public CleanDisconnectingPacket() {
		super("net.client.connection.disconnect.clean");
	}
	
	/**
	 * Creates a new <code>CleanDisconnectingPacket</code>
	 * with the client's sign.
	 * 
	 * @param sign
	 */
	public CleanDisconnectingPacket(String sign) {
		this();
		super.signature = sign;
	}

}
