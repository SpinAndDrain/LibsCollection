package de.spinanddrain.sql;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.spinanddrain.sql.exception.AlreadyConnectedException;
import de.spinanddrain.sql.exception.ConnectionException;
import de.spinanddrain.util.arrays.ArrayUtils;

public class Connection {

	private String hostname, username, database, password;
	private int port;
	private Map<String, Object> properties;
	private java.sql.Connection rawCon;
	
	/**
	 * Creates a new instance with the specified values and not extra
	 * properties.
	 * 
	 * @param hostname the sql hostname
	 * @param port the sql port (default 3306)
	 * @param username the sql user's name
	 * @param password the user's password
	 * @param database the database
	 */
	public Connection(String hostname, int port, String username, String password, String database) {
		this(hostname, port, username, password, database, new HashMap<String, Object>());
	}
	
	/**
	 * Creates a new instance with the specified values.
	 * 
	 * @param hostname the sql hostname
	 * @param port the sql port (default 3306)
	 * @param username the sql user's name
	 * @param password the user's password
	 * @param database the database
	 * @param properties the extra properties (like autoReconnect or useSSL...)
	 */
	public Connection(String hostname, int port, String username, String password, String database, Map<String, Object> properties) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
		this.properties = properties;
	}
	
	/**
	 * 
	 * @return the extra properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	/**
	 * Connects to the database via the specified values.
	 * 
	 * @throws ConnectionException if the task failed
	 */
	public void connect() throws ConnectionException {
		if(isConnected())
			throw new AlreadyConnectedException();
		try {
			rawCon = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + stringifyProps(),
					username, password);
		} catch (SQLException e) {
			throw new ConnectionException(e.getMessage());
		}
	}
	
	/**
	 * Disconnects from the databse.
	 * 
	 * @throws ConnectionException if no connection is established or
	 * 								something with closing the connection
	 * 								went wrong
	 */
	public void disconnect() throws ConnectionException {
		if(!isConnected())
			throw new ConnectionException("no connection established");
		try {
			rawCon.close();
			rawCon = null;
		} catch (SQLException e) {
			throw new ConnectionException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return true if a connection is currently established, false if not
	 */
	public boolean isConnected() {
		return rawCon != null;
	}
	
	/**
	 * Creates a new table if it does not exist.
	 * 
	 * @param name the table's name
	 * @param params the table's parameters
	 * @return the created <code>Table</code> instance
	 * @throws SQLException if something while creating the table went wrong
	 */
	public Table createTable(String name, Parameter... params) throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS " + name + " (";
		for(int i = 0; i < params.length; i++)
			query += params[i] + (i + 1 == params.length ? ")" : ", ");
		if(isConnected()) {
			rawCon.prepareStatement(query).executeUpdate();
		}
		return new Table(name, params);
	}
	
	/**
	 * 
	 * @param primKey primary key value
	 * @param paramName column parameter
	 * @param table the table
	 * @return the value of the specified parameter column and the
	 * 			specified primary key row
	 * @throws SQLException
	 */
	public Object get(Value primKey, String paramName, Table table) throws SQLException {
		if(isConnected()) {
			PreparedStatement ps = rawCon.prepareStatement("SELECT * FROM " + table.getName() +
					" WHERE " + primKey.name + " = ?");
			ps.setObject(1, primKey.value);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getObject(paramName);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param primKey primary key parameter name
	 * @param table the table
	 * @return all primary key entries from the specified table
	 * @throws SQLException
	 */
	public List<String> getKeys(String primKey, Table table) throws SQLException {
		if(isConnected()) {
			PreparedStatement ps = rawCon.prepareStatement("SELECT " + primKey + " FROM " + table.getName());
			List<String> last = new ArrayList<String>();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				last.add(rs.getString(primKey));
			}
			return last;
		}
		return null;
	}
	
	/**
	 * Inserts a new row into the specified table.
	 * 
	 * @param table the table
	 * @param values the column parameters with the new row values
	 * @throws ConnectionException if no connection is established or something went wrong
	 */
	public void insert(Table table, Value... values) throws ConnectionException {
		if(isConnected()) {
			String query = "INSERT INTO " + table.getName() + " (", query1 = ") VALUES (";
			for(int i = 0; i < values.length; i++) {
				query += values[i].name + (i + 1 == values.length ? "" : ", ");
				query1 += "'" + values[i].value + "'" + (i + 1 == values.length ? ")" : ", ");
			}
			try {
				rawCon.prepareStatement(query + query1).executeUpdate();
			} catch (SQLException e) {
				throw new ConnectionException(e.getMessage());
			}
		} else
			throw new ConnectionException("no connection established");
	}
	
	/**
	 * Updates a existing row with the specified values of the specified table.
	 * 
	 * @param table the table
	 * @param primKey the primary key parameter with its value (for the target row)
	 * @param values the values to update (column parameter + new value)
	 * @throws ConnectionException if no connection is established or something went wrong
	 */
	public void update(Table table, Value primKey, Value... values) throws ConnectionException {
		if(isConnected()) {
			for(int i = 0; i < values.length; i++) {
				try {
					PreparedStatement ps = rawCon.prepareStatement("UPDATE " + table.getName() + " SET " + values[i].name + " = ? WHERE "
							+ primKey.name + " = ?");
					ps.setObject(1, values[i].value);
					ps.setObject(2, primKey.value);
					ps.executeUpdate();
				} catch(SQLException e) {
					throw new ConnectionException(e.getMessage());
				}
			}
		} else
			throw new ConnectionException("no connection established");
	}
	
	/**
	 * Deletes a row from the specified table.
	 * 
	 * @param primKey the primary key parameter name and row value
	 * @param table the table
	 * @throws SQLException
	 */
	public void delete(Value primKey, Table table) throws SQLException {
		if(isConnected()) {
			PreparedStatement ps = rawCon.prepareStatement("DELETE FROM " + table.getName() + " WHERE " + primKey.name + " = ?");
			ps.setObject(1, primKey.value);
			ps.executeUpdate();
		}
	}
	
	/**
	 * 
	 * @param value the primary key parameter with the row value
	 * @param table the table
	 * @return true if the specified value (primary key value) is present in the table
	 * @throws SQLException
	 */
	public boolean isRegistered(Value value, Table table) throws SQLException {
		if(isConnected()) {
			PreparedStatement s = rawCon.prepareStatement("SELECT * FROM " + table.getName() + " WHERE "
					+ value.name + " = ?");
			s.setObject(1, value.value);
			return s.executeQuery().next();
		}
		return false;
	}
	
	/**
	 * 
	 * @return the properties to a valid URL string
	 */
	private String stringifyProps() {
		String result = new String();
		if(properties == null || properties.isEmpty())
			return result;
		else
			result += "?";
		for(String k : properties.keySet())
			result += k + "=" + properties.get(k) + "&";
		return ArrayUtils.recreate(ArrayUtils.splitAndModify(result).pop(1).toArray());
	}
	
}
