package de.spinanddrain.updater.exception;

public class UpdateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with the specified message.
	 * 
	 * @param m the message
	 */
	public UpdateException(String m) {
		super(m);
	}
	
	/**
	 * Creates a new instance without a message.
	 * 
	 */
	public UpdateException() {
	}
	
}
