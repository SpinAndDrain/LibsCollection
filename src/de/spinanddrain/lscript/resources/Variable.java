package de.spinanddrain.lscript.resources;

public class Variable {

	private String key, value;
	
	/**
	 * Creates a new variable with a specified name and value.
	 * 
	 * @param key the name
	 * @param value the value
	 */
	protected Variable(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * 
	 * @return the name of the variable
	 */
	public String getName() {
		return key;
	}
	
	/**
	 * 
	 * @return the value of the variable
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Creates a new variable.
	 * 
	 * @param name the name
	 * @param value the value
	 * @return a new variable with the specified name and value
	 */
	public static final Variable of(String name, String value) {
		return new Variable(name, value);
	}
	
}
