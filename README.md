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
