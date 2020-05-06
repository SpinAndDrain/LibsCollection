package de.spinanddrain.sql.exception;

public class ConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with no message.
	 * 
	 */
	public ConnectionException() {
	}
	
	/**
	 * Creates a new instance with a message.
	 * 
	 * @param m the message
	 */
	public ConnectionException(String m) {
		super(m);
	}

}
