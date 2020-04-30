package de.spinanddrain.logging;

import java.util.logging.Logger;

import de.spinanddrain.logging.exception.LoggingException;
import de.spinanddrain.logging.translate.KeyTranslator;

public class Log {

	private Object console;
	private Logger logger;
	private LogType type;
	
	/**
	 * 
	 * @param console a class containing the 'sendMessage(String)' method
	 * @param logger
	 * @param type
	 */
	public Log(Object console, Logger logger, LogType type) {
		this.console = console;
		this.logger = logger;
		this.type = type;
	}
	
	/**
	 * 
	 * @return the console
	 */
	public Object getConsole() {
		return console;
	}
	
	/**
	 * 
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * 
	 * @return the type of logging
	 */
	public LogType getType() {
		return type;
	}
	
	/**
	 * Changes the type of logging to the specified <b>type</b>
	 * 
	 * @param type
	 */
	public void setType(LogType type) {
		this.type = type;
	}
	
	/**
	 * Prints a log without a prefix.
	 * (Format: '[message]')
	 * 
	 * @param message
	 * @see {@link Log#log(String, String)}
	 */
	public void log(String message) {
		this.log(new String(), message);
	}
	
	/**
	 * Prints a log <b>message</b> with the specified log <b>type</b>. If the type does not ignore parameters,
	 * the default parameters {@link KeyTranslator#KEYS} are getting automatically translated.
	 * (Format: '[prefix] [message]')
	 * 
	 * @param prefix
	 * @param message
	 */
	public void log(String prefix, String message) {
		String m = (type == LogType.WARN ? "warn:" : type == LogType.SEVERE ? "severe:" : "") + message;
		try {
			if(type == LogType.RAW) {
				console.getClass().getMethod("sendMessage", String.class).invoke(console, prefix(prefix) + new KeyTranslator(message).translateAll());
			} else {
				KeyTranslator t = new KeyTranslator(m);
				logger.log(t.getLevel(), prefix(prefix) + (type.getIgnoreParameters() ? t.getMessage() : t.translateAll()));
			}
		} catch(Exception e) {
			throw new LoggingException(e.getClass() == NullPointerException.class ? "null" : e.getMessage());
		}
	}
	
	/**
	 * Prints a log in a temporary changed <b>LogType</b>.
	 * 
	 * @param prefix
	 * @param message
	 * @param temporary the temporary changed type
	 * @see {@link Log#log(String, String)}
	 */
	public void logTemporary(String prefix, String message, LogType temporary) {
		final LogType memory = this.type;
		this.setType(temporary);
		this.log(prefix, message);
		this.setType(memory);
	}
	
	private String prefix(String prefix) {
		return prefix == null ? "" : prefix + (prefix.isEmpty() ? "" : " ");
	}
	
}
