package de.spinanddrain.logging;

public enum LogType {
	
	DEFAULT(false),
	RAW(false),
	INFO(true),
	WARN(true),
	SEVERE(true);
	
	private boolean ignoreParameters;
	
	private LogType(boolean ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}
	
	/**
	 * 
	 * @return true if this type ignores translations
	 */
	public boolean getIgnoreParameters() {
		return ignoreParameters;
	}
	
}
