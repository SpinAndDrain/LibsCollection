package de.spinanddrain.sql;

public class Parameter {

	protected String name;
	protected DataType type;
	protected long length;
	protected boolean primaryKey, notNull, autoIncrement;
	protected Object defaultValue;

	/**
	 * Creates a new paramter instance with the specified values.
	 * 
	 * @param name the parameters name
	 * @param type the parameters datatype
	 * @param length the max length of the parameter
	 * @param primaryKey true if this parameter is the primary key
	 * @param notNull true if this parameter's values are not allowed to be null
	 * @param autoIncrement true = allows a unique number to be generated automatically
	 * 						when a new record is inserted into the table (only for numeric values)
	 * @param defaultValue specifies the default value of the parameter (null = no default value)
	 */
	public Parameter(String name, DataType type, long length, boolean primaryKey, boolean notNull,
			boolean autoIncrement, Object defaultValue) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.primaryKey = primaryKey;
		this.notNull = notNull;
		this.autoIncrement = autoIncrement;
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return name + " " + type.toString() + (length < 0 ? "" : "(" + length + ")")
				+ (autoIncrement ? " AUTO_INCREMENT" : "") + (notNull ? " NOT NULL" : "")
				+ (defaultValue != null ? " DEFAULT " + defaultValue : "") + (primaryKey ? " PRIMARY KEY" : "");
	}

}
