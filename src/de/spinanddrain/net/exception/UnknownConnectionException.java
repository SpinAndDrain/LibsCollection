package de.spinanddrain.net.exception;

public class UnknownConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with the specified
	 * message.
	 * 
	 * @param message
	 */
	public UnknownConnectionException(String message) {
		super(message);
	}
	
}
