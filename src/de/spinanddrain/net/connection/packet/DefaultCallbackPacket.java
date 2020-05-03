package de.spinanddrain.net.connection.packet;

public class DefaultCallbackPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -722353160991506757L;

	/**
	 * Creates a new <code>DefaultCallbackPacket</code>.
	 * 
	 */
	public DefaultCallbackPacket() {
		super("net.server.callback.default");
	}

}
