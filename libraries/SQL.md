# SQL

**Version:** 1.0

**Author:** SpinAndDrain

**Description:** A simple library to build a connection to a MySQL database.

### The Use

Here is a small example for a simple database and how to insert, update and get values from it.

First of all, create a new *de.spinanddrain.sql.Connection* instance.

````java
Map<String, Object> props = new HashMap<>();
props.put("autoReconnect", true);
props.put("useSSL", true);

// This is just a test database on my local computer. If you want to let a field empty (as with my password)
// then just define the field as empty String ("" or new String())
Connection connection = new Connection("localhost", 3306, "root", "", "test", props);
````

Now connect to the MySQL server.

````java
try {
	connection.connect();
} catch (ConnectionException e) {
	e.printStackTrace();
}
````

If no exception was thrown, we are connected now. Let's create a table.

````java
try {
	Table table = connection.createTable("testtable",
			new Parameter("id", DataType.VARCHAR, 20, true, true, false, null),
			new Parameter("name", DataType.VARCHAR, 20, false, false, false, null),
			new Parameter("age", DataType.TINYINT, -1, false, false, false, 0));
} catch (SQLException e) {
	e.printStackTrace();
}
````

The created table will look like this now:

id | name | age
-- | ---- | ---

Let's insert 2 entries now.

````java
try {
	connection.insert(table,
			new Value("id", UUID.randomUUID().toString()),
			new Value("name", "Bob"),
			new Value("age", 29));
	connection.insert(table,
			new Value("id", UUID.randomUUID().toString()),
			new Value("name", "Alice"),
			new Value("age", 19));
} catch (ConnectionException e) {
	e.printStackTrace();
}
````

Now our table will looks like this: (The id is random of course)

id | name | age
-- | ---- | ---
bf525ade-ef11-4b1f-b813-7a87df825309 | Bob | 29
9cb1f4dd-f926-492e-b163-4553913d1a16 | Alice | 19

Let's try to print out Bob's age.

````java
byte age = 0;

try {
	age = (byte) connection.get(new Value("id", "bf525ade-ef11-4b1f-b813-7a87df825309"),
			"age", table);
	System.out.println(age);
} catch (SQLException e) {
	e.printStackTrace();
}

// Output: 29
````

We can also print all table entries...

````java
List<String> entries = null;

try {
	entries = connection.getKeys("id", table);
	for(String entry : entries)
		System.out.println(entry);
} catch (SQLException e) {
	e.printStackTrace();
}

/* Output:
 * bf525ade-ef11-4b1f-b813-7a87df825309
 * 9cb1f4dd-f926-492e-b163-4553913d1a16
 */
````

Now let's update Bob's age.

````java
try {
	connection.update(table, new Value("id", "bf525ade-ef11-4b1f-b813-7a87df825309"),
			new Value("age", 44));
} catch (ConnectionException e) {
	e.printStackTrace();
}
````

Our table looks like this now:

id | name | age
-- | ---- | ---
bf525ade-ef11-4b1f-b813-7a87df825309 | Bob | 44
9cb1f4dd-f926-492e-b163-4553913d1a16 | Alice | 19

We can also delete entries.

````java
try {
	connection.delete(new Value("id", "bf525ade-ef11-4b1f-b813-7a87df825309"), table);
} catch (SQLException e) {
	e.printStackTrace();
}
````

Now the table looks like this:

id | name | age
-- | ---- | ---
9cb1f4dd-f926-492e-b163-4553913d1a16 | Alice | 19

If we are done, we can disconnect from the database.

````java
try {
	connection.disconnect();
} catch (ConnectionException e) {
	e.printStackTrace();
}
````

The entire code:

````java
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.spinanddrain.sql.Connection;
import de.spinanddrain.sql.DataType;
import de.spinanddrain.sql.Parameter;
import de.spinanddrain.sql.Table;
import de.spinanddrain.sql.Value;
import de.spinanddrain.sql.exception.ConnectionException;

public class Main {
	
	public static void main(String[] args) {
		
		Map<String, Object> props = new HashMap<>();
		props.put("autoReconnect", true);
		props.put("useSSL", true);
		
		Connection connection = new Connection("localhost", 3306, "root", "", "test", props);
	
		try {
			connection.connect();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
		Table table = null;
		
		try {
			table = connection.createTable("testtable",
					new Parameter("id", DataType.VARCHAR, 20, true, true, false, null),
					new Parameter("name", DataType.VARCHAR, 20, false, false, false, null),
					new Parameter("age", DataType.TINYINT, -1, false, false, false, 0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			connection.insert(table,
					new Value("id", UUID.randomUUID().toString()),
					new Value("name", "Bob"),
					new Value("age", 29));
			connection.insert(table,
					new Value("id", UUID.randomUUID().toString()),
					new Value("name", "Alice"),
					new Value("age", 19));
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
		byte age = 0;
		
		try {
			age = (byte) connection.get(new Value("id", "bf525ade-ef11-4b1f-b813-7a87df825309"),
					"age", table);
			System.out.println(age);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<String> entries = null;
		
		try {
			entries = connection.getKeys("id", table);
			for(String entry : entries)
				System.out.println(entry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			connection.update(table, new Value("id", "bf525ade-ef11-4b1f-b813-7a87df825309"),
					new Value("age", 44));
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
		try {
			connection.delete(new Value("id", "bf525ade-ef11-4b1f-b813-7a87df825309"), table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			connection.disconnect();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
	}
	
}
````

### Useful classes

* [Connection](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/sql/Connection.java)
* [DataType](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/sql/DataType.java)
* [Parameter](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/sql/Parameter.java)
