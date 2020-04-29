package de.spinanddrain.updater.exception;

public class HttpRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with the specified message.
	 * 
	 * @param m the message
	 */
	public HttpRequestException(String m) {
		super(m);
	}
	
}
