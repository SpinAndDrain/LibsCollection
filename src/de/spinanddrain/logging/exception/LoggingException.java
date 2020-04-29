package de.spinanddrain.logging.exception;

public class LoggingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Is thrown if something went wrong while sending a log message during runtime.
	 * 
	 * @param message
	 */
	public LoggingException(String message) {
		super(message);
	}
	
}
