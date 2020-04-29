package de.spinanddrain.lscript.resources;

public class LScriptEntry {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private String key;
	private String value;
	
	/**
	 * Creates a new entry with a specified key and value.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	protected LScriptEntry(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * 
	 * @return the key of this entry
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * 
	 * @return the value of this entry
	 */
	public String getValue() {
		return value;
	}
	
}
