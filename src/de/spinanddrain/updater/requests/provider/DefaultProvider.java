package de.spinanddrain.updater.requests.provider;

import java.io.File;

public class DefaultProvider implements ExecutionProvider {

	private String name;
	
	/**
	 * Creates a new instance with the default file name prefix "Plugin".
	 * 
	 */
	public DefaultProvider() {
		this("Plugin");
	}
	
	/**
	 * Creates a new instance with the specified file name prefix.
	 * 
	 * @param name
	 */
	public DefaultProvider(String name) {
		this.name = name;
	}
	
	@Override
	public String preparation() {
		return name;
	}
	
	@Override
	public void postProcessing(File f) {
	}
	
}
