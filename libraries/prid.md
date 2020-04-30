# prid

**Version:** 1.0

**Author:** SpinAndDrain

**Description:** A simple parser to convert data strings.

### The Use

__prid__ was made to parse resource id's out of a long string chain. So the field of application is not very large
because it is very specific. __prid__ can parse the following format:

```
key:value;key2:value2;key3:value3...
```

Each key is mostly formated as a package name and each value is an Integer. Here is a quick example how I use it:

```
de.spinanddrain.supportchat:60569;de.spinanddrain.advancedlog:64268;de.spinanddrain.simpleauth:64810;de.spinanddrain.libscollection:78115;
```

As you can see, the key is the respective main package of the respective project and the value is the resource id.
__prid__ can fetch this information out of any URL on a web server and so I can always get the resource ids
just with the repsective key.

The idea behind __prid__ was that every time you puplish a new resource on SpigotMC.org you had directly to
make an update to set the correct resource id in your plugin's updater. With __prid__ you can insert the resource id
after you created the resource on SpigotMC, and so no update is needed.

Currently __prid__ is only able to read the resources out of my web server.
Nevertheless I'll show quick how to use it:

````java
// This will return the resource id of LibsCollection
int resource = ResourceIdParser.defaultPrid().getResourceIdByKey("de.spinanddrain.libscollection");
````

### Useful classes
* (ResourceIdParser)[https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/prid/ResourceIdParser.java]
