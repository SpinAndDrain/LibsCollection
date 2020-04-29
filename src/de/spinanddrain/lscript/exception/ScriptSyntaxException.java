package de.spinanddrain.lscript.exception;

public class ScriptSyntaxException extends ScriptException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4084152130207204182L;

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	/**
	 * Creates a new instance with the specified message and
	 * the specified line.
	 * 
	 * @param message the message
	 * @param line the line
	 */
	public ScriptSyntaxException(String message, int line) {
		super(message, line);
	}
	
}
