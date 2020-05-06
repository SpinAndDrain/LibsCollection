package de.spinanddrain.sql.exception;

public class AlreadyConnectedException extends ConnectionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with no message.
	 * 
	 */
	public AlreadyConnectedException() {
	}
	
	/**
	 * Creates a new instance with a message.
	 * 
	 * @param m the message
	 */
	public AlreadyConnectedException(String m) {
		super(m);
	}
	
}
