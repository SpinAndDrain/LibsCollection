package de.spinanddrain.lscript.resources;

import java.util.ArrayList;
import java.util.List;

public class LContainer {

	protected String name;
	protected LScriptEntry[] content;
	
	/**
	 * Creates a new container with the specified name and content.
	 * 
	 * @param name the name of this container
	 * @param content the entries of this container
	 */
	public LContainer(String name, LScriptEntry[] content) {
		this.name = name;
		this.content = content;
	}
	
	/**
	 * 
	 * @return the name of the container
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return the content of the container as array
	 */
	public LScriptEntry[] getContent() {
		return content;
	}
	
	/**
	 * 
	 * @param key
	 * @return the value of the specified key; null if the container does not contain such a key
	 */
	public LScriptEntry getByKey(String key) {
		for(LScriptEntry e : content) {
			if(e.getKey().equals(key)) {
				return e;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return a new copy of this container which is modifiable
	 */
	public ModifiableContainer modify() {
		return new ModifiableContainer(this);
	}
	
	public static class ContainerBuilder {
		
		private String name;
		private List<LScriptEntry> content;
		
		/**
		 * Creates a new instance with the specified container name.
		 * 
		 * @param name the containers name
		 */
		public ContainerBuilder(String name) {
			this.name = name;
			this.content = new ArrayList<LScriptEntry>();
		}
		
		/**
		 * 
		 * @return the name of the container
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * 
		 * @param c
		 * @return adds an entry <b>c</b> to the container
		 */
		public ContainerBuilder add(LScriptEntry c) {
			content.add(c);
			return this;
		}
		
		/**
		 * 
		 * @see {@link ContainerBuilder#add(LScriptEntry)}
		 * @param key {@link LScriptEntry#getValue()}
		 * @param value {@link LScriptEntry#getValue()}
		 * @return {@link this}
		 */
		public ContainerBuilder add(String key, String value) {
			content.add(new LScriptEntry(key, value));
			return this;
		}
		
		/**
		 * Removes the specified entry out of this builder.
		 * 
		 * @param c entry
		 * @return {@link this}
		 */
		public ContainerBuilder remove(LScriptEntry c) {
			if(content.contains(c)) {
				content.remove(c);
			}
			return this;
		}
		
		/**
		 * Removes the specified entry out of this builder.
		 * 
		 * @param key key of the entry
		 * @return {@link this}
		 */
		public ContainerBuilder remove(String key) {
			for(LScriptEntry c : content) {
				if(c.getKey().equals(key)) {
					content.remove(c);
				}
			}
			return this;
		}
		
		/**
		 * Changes a entrys value.
		 * 
		 * @param key the key of the affected entry
		 * @param newValue the new changed value
		 * @return {@link this}
		 */
		public ContainerBuilder set(String key, String newValue) {
			for(int i = 0; i < content.size(); i++) {
				if(content.get(i).getKey().equals(key)) {
					content.set(i, new LScriptEntry(key, newValue));
				}
			}
			return this;
		}
		
		/**
		 * 
		 * @param key
		 * @return true if the container contains the specified key, false if not
		 */
		public boolean containsKey(String key) {
			for(LScriptEntry e : content) {
				if(e.getKey().equals(key)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 
		 * @return builds and returns the final result as {@link LContainer}
		 */
		public LContainer build() {
			return new LContainer(name, content.toArray(new LScriptEntry[content.size()]));
		}
		
	}
	
	public static class CollectiveContainerBuilder {

		private final List<ContainerBuilder> containers;
		
		/**
		 * Creates a new instance of a array of <code>ContainerBuilder</code>s.
		 * 
		 */
		public CollectiveContainerBuilder() {
			this.containers = new ArrayList<ContainerBuilder>();
		}
		
		/**
		 * Adds a entry to the specified container name. If the container does
		 * not exist, it gets created.
		 * 
		 * @param containerName the name of the container
		 * @param entry
		 * @return this
		 * @see {@link ContainerBuilder#add(LScriptEntry)}
		 */
		public CollectiveContainerBuilder add(String containerName, LScriptEntry entry) {
			ContainerBuilder builder = this.find(containerName);
			if(builder == null)
				containers.add(new ContainerBuilder(containerName).add(entry));
			else
				builder.add(entry);
			return this;
		}
		
		/**
		 * Adds a new entry by the specified key and value to the specified container name.
		 * If the container does not exist, it gets created.
		 * 
		 * @param containerName the name of the container
		 * @param key the entrys key
		 * @param value the entrys value
		 * @return this
		 * @see {@link ContainerBuilder#add(String, String)}
		 */
		public CollectiveContainerBuilder add(String containerName, String key, String value) {
			this.add(containerName, new LScriptEntry(key, value));
			return this;
		}
		
		/**
		 * Removes a entry out of the container by the specified container name.
		 * If the container is empty after this action, it gets removed.
		 * 
		 * @param containerName the name of the container
		 * @param entry
		 * @return this
		 * @see {@link ContainerBuilder#remove(LScriptEntry)}
		 */
		public CollectiveContainerBuilder remove(String containerName, LScriptEntry entry) {
			ContainerBuilder builder = this.find(containerName);
			if(builder != null) {
				builder.remove(entry);
				if(builder.content.isEmpty()) {
					containers.remove(builder);
				}
			}
			return this;
		}
		
		/**
		 * Removes a entry by the specified key out of the container by the specified container name.
		 * If the container is empty after this action, it gets removed.
		 * 
		 * @param containerName the name of the container
		 * @param key the key of the entry
		 * @return this
		 * @see {@link ContainerBuilder#remove(String)}
		 */
		public CollectiveContainerBuilder remove(String containerName, String key) {
			ContainerBuilder builder = this.find(containerName);
			if(builder != null) {
				builder.remove(key);
				if(builder.content.isEmpty()) {
					containers.remove(builder);
				}
			}
			return this;
		}
		
		/**
		 * Changes a entry to the specified new value.
		 * 
		 * @param containerName the name of the container
		 * @param key the key of the entry
		 * @param newValue the new value
		 * @return this
		 * @see {@link ContainerBuilder#set(String, String)}
		 */
		public CollectiveContainerBuilder set(String containerName, String key, String newValue) {
			ContainerBuilder builder = this.find(containerName);
			if(builder != null) {
				builder.set(key, newValue);
			}
			return this;
		}
		
		/**
		 * Builds this class.
		 * 
		 * @return a new array of all created containers and their entries
		 */
		public LContainer[] pack() {
			LContainer[] array = new LContainer[containers.size()];
			for(int i = 0; i < array.length; i++) {
				array[i] = containers.get(i).build();
			}
			return array;
		}
		
		/**
		 * Finds a container if it is not null.
		 * 
		 * @param name
		 * @return containers#byName
		 */
		public ContainerBuilder find(String name) {
			for(ContainerBuilder builder : containers) {
				if(builder.getName().equals(name)) {
					return builder;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return creates and returns a sub container with one entry (key, value)
	 */
	public static LContainer subContainer(String key, String value) {
		return new LContainer("$localSubContainer", new LScriptEntry[]{new LScriptEntry(key, value)});
	}
	
}
