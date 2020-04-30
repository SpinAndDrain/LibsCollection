# Logging

**Version:** 1.0

**Author:** SpinAndDrain

**Description:** A simple logging extension to format your logs and console outputs.

### The Use

Example:

````java
// Creating a Log instance, first argument the console itself, second the logger
Log logger = new Log(Bukkit.getConsoleSender(), Bukkit.getLogger(), LogType.DEFAULT);

// Print a normal log
logger.log(KeyTranslator.INFO + "This is a info log.");
logger.log("[MyPlugin]", KeyTranslator.WARN + "This is a warning log with prefix.");
logger.log(KeyTranslator.SEVERE + "This is a severe log, watch out!");

// To print via the console command sender, use LogType.RAW
logger.logTemporary("[MyPlugin]", KeyTranslator.GREEN + "This message will appear greeeen.", LogType.RAW);

// You can also directly switch the LogType if you do not want to log temporary each time
logger.setType(LogType.RAW);
logger.log("Btw, you can still use the default coloring like this: §cRed");
````

**LogType:**

* __DEFAULT__ The default LogType. The Level of the message can be set by a level key at the start of the message.
* __RAW__ The LogType to print logs via the console. Color Codes are possible here.
* __INFO__ Prints the logs as "INFO".
* __WARN__ Prints the logs as "WARNING".
* __SEVERE__ Prints the logs as "SEVERE".

### Useful classes

* [Log](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/logging/Log.java)
* [LogType](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/logging/LogType.java)
* [KeyTranslator](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/logging/translate/KeyTranslator.java)
