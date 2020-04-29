package de.spinanddrain.lscript.exception;

public class ScriptException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with the specified message and
	 * the specified line.
	 * 
	 * @param m the message
	 * @param line the line
	 */
	public ScriptException(String m, int line) {
		super("[Line:" + line + "] " + m);
	}
	
}
