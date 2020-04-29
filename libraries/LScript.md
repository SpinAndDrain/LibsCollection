# LScript

**Version:** 2.0

**Author:** SpinAndDrain

**Description:** A small script language to store language packets for applications.

### The Use

LScript has 2 file formats: **Pattern (.ls)** and **Translation (.lang)**. The difference
between these formats is not high.

Each script contains out of a *header* and a *body* section. The *header* contains script informations and
variables. Variables can be used as placeholders in the *body* section later. The *body* section contains
the translations. These can be sorted in *containers* to get a better overview as stranger. The default *container*
is **main**. You can comment with **#** everywhere in all **LScript** files.

**Example**: (myplugin_english.lang)
```
# With @type you define the name and/or the version of the script.
# This is a String and can be completely invented.
# Important: This argument is required in translations scripts only.
@type(MyPlugin-1.0)

# With @pattern you define the name and/or the version of the pattern script.
# This is a String and can be completely invented.
# Important: This argument is required in all scripts.
@pattern(Pattern-MyPlugin-1.0)

# This is the Variables-Section. Variables are not required
# and serve as placeholders.
# Format: variablename: 'variablevalue'
# The '' which enclose the variable value are only required if your value contains spaces.
prefix: [MyPlugin]
suffix: 'This is my suffix'

# END OF HEADER
# START OF BODY

# This is a container (the main container).
# One container per script is required.
# The container opens with a '{' and closes with a '}'.
# The entries are defined between those.
main {
    # The entries are defined like the variables.
    # You can insert the variables using a $ and the variable name.
    # Formats:
    # - entryname: 'entryvalue'
    # - entryname = 'entryvalue'
    no-permission: '$prefix Sorry but you do not have permission to perform this command! $suffix'
    anymessage = Hey
    anothermessage: whats
    foo: 'up $suffix'
}

another_container {
    another-message: 'Hello World'
}

# You can add as many containers and entries as you want (and variables of course ;p )
# ...
# Thats it.
```

Without comments:
```
@type(MyPlugin-1.0)
@pattern(Pattern-MyPlugin-1.0)

prefix: [MyPlugin]
suffix: 'This is my suffix'

main {
    no-permission: '$prefix Sorry but you do not have permission to perform this command! $suffix'
    anymessage = Hey
    anothermessage: whats
    foo: 'up $suffix'
}

another_container {
    another-message: 'Hello World'
}
```

**Getting the values:**
You simply can get the values of scripts by using the **LReader** and **LParser**:
(package de.spinanddrain.lscript.tools)
````java
try {
	// reading and parsing the written script
	File script = new File("anyFolder/myplugin_english.lang");
	LParser parser = new LReader(script).readAndParse(ScriptType.TRANSLATION);
			
	// Get the type and the pattern
	String type = parser.getVersionType();
	String pattern = parser.getPattern();
			
	// Get the variables
	Variable[] variables = parser.getVariables();
			
	// Get container by name
	LContainer main = parser.getContainerByName("main");
			
	// Get entry out of container
	LScriptEntry noPermission = main.getByKey("no-permission");
			
	 /* Get entry without knowing the entry's container
		* If the same entry key is defined in multiple 
		* containers, the first found entry gets returned
		*/
	String anyMessage = parser.getByKey("anymessage");
			
} catch (FileNotFoundException | ScriptException | FileNotSupportedException e) {
	e.printStackTrace();
}
````
