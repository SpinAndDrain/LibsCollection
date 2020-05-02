package de.spinanddrain.net.connection.packet;

public class PingPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5420671976954757964L;

	/**
	 * Creates a new <code>PingPacket</code>.
	 * 
	 */
	public PingPacket() {
		super("net.server.keepalive.ping");
	}

}
