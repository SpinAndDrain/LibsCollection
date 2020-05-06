# Updater

**Version:** 1.0.3

**Author:** SpinAndDrain

**Description:** A Updater for Spigot & BungeeCord to check versions and install updates.

### The Use

Create a new instance of the __Updater__ class.

````java
// '12345' = resource id
// '1.0' = the current version => MainClass#getDescription().getVersion();
Updater updater = new Updater(12345, "1.0");
````

Check if a newer version is available:

````java
if(updater.isAvailable()) {
	// Newer version is available
}
````

Installing the latest version on Spigot:

````java
// "MyPlugin" = name of the new installation
updater.installLatestVersion(new Spigot(MainClass.getInstance(), "MyPlugin"));
// This method disables and deletes the current plugin, downloads the latest version and enables the latest version
````

Installing the latest version on BungeeCord:

````java
// "MyPlugin" = name of the new installation
updater.installLatestVersion(new Bungee(MainClass.getInstance(), "MyPlugin"));
// This method downloads the latest version and deletes the old plugin on shutdown - restart required
````

### Useful classes

* [Updater](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/updater/Updater.java)
* [VersionPattern](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/updater/VersionPattern.java)
