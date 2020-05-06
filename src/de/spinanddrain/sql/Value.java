package de.spinanddrain.sql;

public class Value {

	protected String name;
	protected Object value;
	
	/**
	 * Creates a new instance with the specified values.
	 * 
	 * @param name the name of the value/entry (= the same as the parameter name)
	 * @param value the value of the value/entry
	 */
	public Value(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * 
	 * @return the name of the value/entry (= the same as the parameter name)
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * 
	 * @return value the value of the value/entry
	 */
	public String getName() {
		return name;
	}
	
}
