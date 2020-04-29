package de.spinanddrain.lscript.resources;

public class ModifiableContainer extends LContainer {

	/**
	 * Creates a new container that is modifiable.
	 * 
	 * @param name the name of the container
	 * @param content the current content of the container
	 */
	public ModifiableContainer(String name, LScriptEntry[] content) {
		super(name, content);
	}

	/**
	 * Creates a new container that is modifiable out of the parents
	 * content.
	 * 
	 * @param parent the pre-filled container
	 */
	public ModifiableContainer(LContainer parent) {
		super(parent.name, parent.content);
	}
	
	/**
	 * Changes a entrys value.
	 * 
	 * @param key the key of the entry
	 * @param newValue the new value of the entry
	 */
	public void set(String key, String newValue) {
		int index = this.find(key);
		if(index >= 0) {
			super.content[index] = new LScriptEntry(key, newValue);
		}
	}
	
	/**
	 * Adds a new entry to this container.
	 * 
	 * @param key the new key
	 * @param value the new value
	 */
	public void add(String key, String value) {
		LScriptEntry[] copy = new LScriptEntry[super.content.length + 1];
		for(int i = 0; i < super.content.length; i++) {
			copy[i] = super.content[i];
		}
		copy[super.content.length] = new LScriptEntry(key, value);
		super.content = copy;
	}
	
	/**
	 * Removes a entry out of this container if its a part of this container.
	 * 
	 * @param key the key of the entry
	 */
	public void remove(String key) {
		if(this.find(key) < 0) {
			return;
		}
		LScriptEntry[] copy = new LScriptEntry[super.content.length - 1];
		for(int i = 0, c = 0; i < super.content.length; i++) {
			if(!super.content[i].getKey().equals(key)) {
				copy[c++] = super.content[i];
			}
		}
		super.content = copy;
	}
	
	/**
	 * Finds the array index of the specified entry.
	 * 
	 * @param key the key of the entry
	 * @return the array index of the entry in this container
	 */
	public int find(String key) {
		for(int i = 0; i < super.content.length; i++) {
			if(super.content[i].getKey().equals(key)) {
				return i;
			}
		}
		return -1;
	}
	
}
