package de.spinanddrain.sql;

public class Table {

	private String name;
	private Parameter[] params;
	
	/**
	 * Creates a new instance with the specified values.
	 * 
	 * @param name the name of the table
	 * @param params the parameters of the table
	 */
	public Table(String name, Parameter... params) {
		this.name = name;
		this.params = params;
	}
	
	/**
	 * 
	 * @return the name of the table
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return the parameters of the table
	 */
	public Parameter[] getParameters() {
		return params;
	}
	
}
