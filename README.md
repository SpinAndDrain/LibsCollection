# LibsCollection
A collection of useful libraries and functions for the Spigot and BungeeCord plugin
developement.

### Links

* [Resource Page](https://www.spigotmc.org/resources/libscollection-all-versions-spigot-bungeecord.78115/)
* [Mini-Documentations](https://github.com/SpinAndDrain/LibsCollection/tree/master/libraries)

### Dependencies

To use __LibsCollection__ to the full extent you simply need a Spigot or BungeeCord
Server. Just put the downloaded .jar file from the resource page in your plugins
folder.

### The Use

If you are working with this plugin, add __LibsCollection__ as a dependency in
your plugin.yml or bungee.yml:

````yml
# plugin.yml
depend: [LibsCollection]
````

````yml
# bungee.yml
depends: [LibsCollection]
````

Add the __LibsCollection__ jar file to your Java Build Path or add the dependency
to your maven project:

````xml
	<repositories>
		<repository>
			<id>jitpack.io</id>
			<url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependencies>
		<dependency>
			<groupId>com.github.SpinAndDrain</groupId>
			<artifactId>LibsCollection</artifactId>
			<version>1.2</version>
		</dependency>
	</dependencies>
````

Now you can use all the classes and methods from __LibsCollection__. See the
source code for informations.

### Compatibility

__LibsCollection__ is (currently) compatible with all minecraft versions
(Spigot & BungeeCord). If __LibsCollection__ is used as a dependency of 
another plugin, then the total compatibility can be influenced by the other
plugins version scope.

### Included Libraries

Library | Version | Author | Description
------- | ------- | ------ | -----------
**[Logging](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/Logging.md)** | 1.0.1 | SpinAndDrain | A simple logging extension to format your logs and console outputs.
**[LScript](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/LScript.md)** | 2.0.3 | SpinAndDrain | A small script language to store language packets for applications.
**[prid](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/prid.md)** | 1.0 | SpinAndDrain | A simple parser to convert data strings.
**[Updater](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/Updater.md)** | 1.0.3 | SpinAndDrain | A Updater for Spigot & BungeeCord to check versions and install updates.
**[UTIL](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/UTIL.md)** | 1.0 | SpinAndDrain | API for modifying and handling Arrays, Strings and mathematical processes (and much more).
**[net](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/net.md)** | 1.0 | SpinAndDrain | A simple Client-Server Socket tool to send packets through a network.
**SQL** | 1.0 | SpinAndDrain | A simple library to build a connection to a MySQL database.
