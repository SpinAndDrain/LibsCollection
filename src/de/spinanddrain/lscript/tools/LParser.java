package de.spinanddrain.lscript.tools;

import java.util.ArrayList;
import java.util.List;

import de.spinanddrain.lscript.LScript;
import de.spinanddrain.lscript.resources.LContainer;
import de.spinanddrain.lscript.resources.LScriptEntry;
import de.spinanddrain.lscript.resources.ModifiableContainer;
import de.spinanddrain.lscript.resources.Variable;
import de.spinanddrain.lscript.resources.LContainer.ContainerBuilder;

public class LParser {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private LScriptEntry versionType, pattern;
	private LContainer[] content;
	private Variable[] variables;
	
	/**
	 * Creates a new instance with the specified content and automatically
	 * replacing the variables.
	 * 
	 * @param content
	 */
	public LParser(LContainer... content) {
		this(true, content);
	}
	
	/**
	 * Creates a new instance with the specified content and a option if
	 * the variables should be replaced automatically.
	 * 
	 * @param replaceVariables true = replace automatically
	 * @param content
	 */
	public LParser(boolean replaceVariables, LContainer... content) {
		for(LScriptEntry e : content[0].getContent()) {
			if(e.getKey().equals("!type")) {
				this.versionType = e;
			} else if(e.getKey().equals("!pattern")) {
				this.pattern = e;
			}
		}
		LScriptEntry[] vars = remove(content[0], versionType, pattern).getContent();
		this.variables = new Variable[vars.length];
		for(int i = 0; i < vars.length; i++) {
			this.variables[i] = Variable.of(vars[i].getKey(), vars[i].getValue());
		}
		this.content = replaceVariables ? replaceVariables(content) : reworkRaw(content);
	}
	
	/**
	 * 
	 * @return true if this parser has emerged by a translation file (.lang)
	 */
	public boolean hasVersionType() {
		return versionType != null;
	}
	
	/**
	 * 
	 * @return true if this parser has emerged by a pattern file (.ls)
	 */
	public boolean hasPattern() {
		return pattern != null;
	}
	
	/**
	 * 
	 * @return the type-version of the translation file, null if this parser has not emerged by a translation file
	 */
	public String getVersionType() {
		return versionType.getValue();
	}
	
	/**
	 * 
	 * @return the type-version of the pattern file, null if this parser has not emerged by a pattern file
	 */
	public String getPattern() {
		return pattern.getValue();
	}
	
	/**
	 * 
	 * @return the content of the file
	 * @see {@link LReader#read(ScriptType)}
	 */
	public LContainer[] getContent() {
		return content;
	}
	
	/**
	 * 
	 * @return the variables of the file
	 */
	public Variable[] getVariables() {
		return variables;
	}
	
	/**
	 * 
	 * @param name
	 * @return the container with the specified <b>name</b>, null if no such container was found
	 */
	public LContainer getContainerByName(String name) {
		for(LContainer c : content) {
			if(c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param key
	 * @return the first value of the specified <b>key</b> of all containers in this file
	 * @see {@link LContainer#getByKey(String)}
	 */
	public String getByKey(String key) {
		for(int i = 0; i < content.length; i++) {
			if(content[i].getByKey(key) != null) {
				return content[i].getByKey(key).getValue();
			}
		}
		return null;
	}
	
	/**
	 * Replaces all variables for all entries.
	 * 
	 * @param entries
	 * @return the new replaced content
	 */
	private LContainer[] replaceVariables(LContainer[] entries) {
		LContainer[] reworked = new LContainer[entries.length - 1];
		List<LScriptEntry> entrys = new ArrayList<LScriptEntry>();
		for(int i = 1; i < entries.length; i++) {
			for(LScriptEntry e : entries[i].getContent()) {
				String value = e.getValue();
				for(Variable v : variables) {
					if(value.contains("$" + v.getName())) {
						value = value.replace("$" + v.getName(), v.getValue());
					}
				}
				entrys.add(LScript.create(e.getKey(), value));
			}
			reworked[i - 1] = new LContainer(entries[i].getName(), entrys.toArray(new LScriptEntry[entrys.size()]));
			entrys.clear();
		}
		return reworked;
	}
	
	/**
	 * 
	 * @param entries
	 * @return the raw content without replace any variables
	 */
	private LContainer[] reworkRaw(LContainer[] entries) {
		LContainer[] reworked = new LContainer[entries.length - 1];
		for(int i = 0; i < reworked.length; i++) {
			reworked[i] = entries[i+1];
		}
		return reworked;
	}
	
	/**
	 * Remove entries from this container.
	 * 
	 * @param content
	 * @param e
	 * @return the new modified container
	 */
	private LContainer remove(LContainer content, LScriptEntry... e) {
		ModifiableContainer container = new ModifiableContainer(content);
		for(LScriptEntry x : e) {
			if(x != null)
				container.remove(x.getKey());
		}
		return container;
	}
	
	public static final class VirtualParsingSession {
		
		private String type, pattern;
		private Variable[] variables;
		private LContainer[] content;
		private boolean replaceVariables;
		
		/**
		 * Creates a new instance with the specified values.
		 * 
		 * @param pattern the pattern of the virtual script
		 * @param variables the variables of the virtual script
		 * @param content the content of the virtual script
		 * @param replaceVariables {@link LParser#LScriptParser(boolean, LContainer...)}
		 */
		public VirtualParsingSession(String type, String pattern, Variable[] variables, LContainer[] content, boolean replaceVariables) {
			this.type = type;
			this.pattern = pattern;
			this.variables = variables;
			this.content = content;
			this.replaceVariables = replaceVariables;
		}
		
		/**
		 * Creates a <code>LScriptParser</code> instance of an virtual script.
		 * 
		 * @return the new <code>LScriptParser</code> instance
		 */
		public LParser virtualize() {
			LContainer[] c = new LContainer[content.length + 1];
			ContainerBuilder builder = new ContainerBuilder("$raw");
			if(type != null)
				builder.add("!type", type);
			if(pattern != null)
				builder.add("!pattern", pattern);
			for(Variable v : variables) {
				builder.add(v.getName(), v.getValue());
			}
			c[0] = builder.build();
			for(int i = 0; i < content.length; i++) {
				c[i+1] = content[i];
			}
			return new LParser(replaceVariables, c);
		}
		
	}
	
}
