# LibsCollection
A collection of useful libraries and functions for the Spigot and BungeeCord plugin
developement.

### Links

* [Resource Page](?)

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
			<version>master-SNAPSHOT</version>
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

Library | Version | Description
------- | ------- | -----------
**Logging** | 1.0 | A simple logging extension to format your logs and console outputs.
**LScript** | 2.0 | A small script language to store language packets for applications.
**prid** | 1.0 | A simple parser to convert data strings.
**Updater** | 1.0 | A Updater for Spigot & BungeeCord to check versions and install updates.
**UTIL** | 1.0 | API for modifying and handling Arrays, Strings and mathematical processes (and much more).
