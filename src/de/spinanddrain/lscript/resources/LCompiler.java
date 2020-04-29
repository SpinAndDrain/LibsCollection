package de.spinanddrain.lscript.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.PatternSyntaxException;

import de.spinanddrain.lscript.exception.ScriptCompileException;
import de.spinanddrain.lscript.exception.ScriptDecompileException;
import de.spinanddrain.lscript.exception.ScriptException;
import de.spinanddrain.lscript.exception.ScriptSyntaxException;
import de.spinanddrain.lscript.resources.LContainer.CollectiveContainerBuilder;
import de.spinanddrain.lscript.tools.LParser;
import de.spinanddrain.util.arrays.ArrayUtils;
import de.spinanddrain.util.arrays.StringArray;
import de.spinanddrain.util.holding.Memorizer;

public final class LCompiler {
	
	private Session s;
	private LCompilerProperties properties;
	private AtomicInteger kc, vc, ec, cc, cl;
	
	/**
	 * Creates a new instance with the specified session and the
	 * default new instance of <code>LCompilerProperties</code>.
	 * 
	 * @param session
	 * @param properties
	 */
	public LCompiler(Session session) {
		this(session, new LCompilerProperties());
	}
	
	/**
	 * Creates a new instance with the specified session and
	 * the specified <code>LCompilerProperties</code>.
	 * 
	 * @param session
	 * @param properties
	 */
	public LCompiler(Session session, LCompilerProperties properties) {
		this.s = session;
		this.properties = properties;
		this.resetSession();
	}
	
	/**
	 * Changes the current session to the new session and
	 * the new properties.
	 * 
	 * @param newSession the new session
	 */
	public void reInitSession(Session newSession, LCompilerProperties properties) {
		this.s = newSession;
		this.properties = properties;
	}
	
	/**
	 * 
	 * @return the current session
	 */
	public Session getSession() {
		return s;
	}
	
	/**
	 * Compiles the specified session by the specified properties.
	 * 
	 * @return a new <code>String[]</code> containing each compiled line
	 * 			of the given values
	 */
	public String[] compile() {
		if(!(s instanceof CompilingSession)) {
			throw new ScriptCompileException("the current session is invalid");
		}
		resetSession();
		List<String> lines = new ArrayList<String>();
		final LParser p = ((CompilingSession) s).p;
		if(p.hasVersionType()) {
			if(!keyOverflow())
				lines.add(getKeyBegin() + "type(" + p.getVersionType() + ")");
			else
				throw new ScriptCompileException("key limit overflowed");
		}
		if(p.hasPattern()) {
			if(!keyOverflow())
				lines.add(getKeyBegin() + "pattern(" + p.getPattern() + ")");
			else
				throw new ScriptCompileException("key limit overflowed");
		}
		if(kc.get() < getKeyMin())
			throw new ScriptCompileException("missing necessary argument");
		lines.add(new String());
		for(String c : getComments()) {
			if(commentOverflow())
				throw new ScriptCompileException("comment limit overflowed");
			lines.add(getCommentBegin() + " " + c);
		}
		if(cl.get() > 0)
			lines.add(new String());
		for(Variable v : p.getVariables()) {
			if(variableOverflow())
				throw new ScriptCompileException("variable limit overflowed");
			if(!isValidVariableName(v.getName()))
				throw new ScriptCompileException("variable has invalid name: '" + v.getName() + "'");
			lines.add(v.getName() + getVariableSplit() + convertValue(v.getValue()));
		}
		if(vc.get() > 0)
			lines.add(new String());
		int cx = 0;
		for(LContainer c : p.getContent()) {
			if(containerOverflow())
				throw new ScriptCompileException("container limit overflowed");
			if(!isValidContainerName(c.getName()))
				throw new ScriptCompileException("container has invalid name: '" + c.getName() + "'");
			lines.add(c.getName() + " " + getContainerOpening(false));
			for(LScriptEntry e : c.getContent()) {
				if(entryOverflow())
					throw new ScriptCompileException("entry limit overflowed");
				if(!isValidEntryName(e.getKey()))
					throw new ScriptCompileException("entry has invalid name: '" + e.getKey() + "'");
				lines.add("\t" + e.getKey() + getEntrySplit() + convertValue(e.getValue()));
			}
			lines.add(getContainerClosure(false));
			if(cx++ + 1 < p.getContent().length)
				lines.add(new String());
		}
		return lines.toArray(new String[lines.size()]);
	}
	
	public LContainer[] decompile() throws ScriptException {
		if(!(s instanceof DecompilingSession)) {
			throw new ScriptDecompileException("the current session is invalid");
		}
		resetSession();
		CollectiveContainerBuilder builder = new CollectiveContainerBuilder();
		final String[] lines = ((DecompilingSession) s).lines;
		String currentContainer = null, currentLine = null;
		for(int i = 0, c = 1; i < lines.length; i++, c++) {
			currentLine = ignoreSpacesAndComments(lines[i]);
			if(currentLine.isEmpty() || currentLine.startsWith(getCommentBegin())) {
				continue;
			}
			if(currentContainer == null) {
				if(currentLine.startsWith(getKeyBegin()) && currentLine.contains("(") && currentLine.contains(")")) {
					if(keyOverflow())
						throw new ScriptDecompileException("key limit overflowed");
					if(currentLine.contains("type"))
						builder.add("$raw", "!type", getFirstContentBetween(currentLine, "()"));
					else if(currentLine.contains("pattern"))
						builder.add("$raw", "!pattern", getFirstContentBetween(currentLine, "()"));
					else
						throw new ScriptSyntaxException("invalid key word", c);
				} else if(containsAny(currentLine, getVariableSplit()) && splitExact(
						currentLine, getActiveSplitValue(currentLine, getVariableSplit())).length == 2) {
					String[] v = splitExact(currentLine, getActiveSplitValue(currentLine, getVariableSplit()));
					String name = v[0];
					if(variableOverflow())
						throw new ScriptDecompileException("variable limit overflowed");
					if(builder.find("$raw") != null && builder.find("$raw").containsKey(name))
						throw new ScriptException("variable '" + name + "' was already initialized", c);
					if(!isValidVariableName(name))
						throw new ScriptSyntaxException("invalid variable name '" + name + "'", c);
					builder.add("$raw", name, getSymbolSize('\'', v[1]) == 2 ? getFirstContentBetween(v[1], "''") : v[1]);
				} else if(currentLine.endsWith(getContainerOpening(false)) && currentLine.split(getContainerOpening(true)).length == 1) {
					String name = currentLine.split(getContainerOpening(true))[0];
					if(kc.get() < getKeyMin())
						throw new ScriptDecompileException("missing necessary argument");
					if(containerOverflow())
						throw new ScriptDecompileException("container limit overflowed");
					if(builder.find(name) != null)
						throw new ScriptException("container '" + name + "' was already initialized", c);
					if(!isValidContainerName(name))
						throw new ScriptSyntaxException("invalid container name '" + name + "'", c);
					currentContainer = name;
				} else
					throw new ScriptSyntaxException("invalid syntax '" + currentLine + "'", c);
			} else {
				if(containsAny(currentLine, getEntrySplit()) &&
						splitExact(currentLine, getActiveSplitValue(currentLine, getEntrySplit())).length == 2) {
					String[] v = splitExact(currentLine, getActiveSplitValue(currentLine, getEntrySplit()));
					String key = v[0];
					if(variableOverflow())
						throw new ScriptDecompileException("entry limit overflowed");
					if(builder.find(currentContainer) != null && builder.find(currentContainer).containsKey(key))
						throw new ScriptException("entry key '" + key + "' was already initialized", c);
					if(!isValidEntryName(key))
						throw new ScriptSyntaxException("invalid entry key name '" + key + "'", c);
					builder.add(currentContainer, key, getSymbolSize('\'', v[1]) == 2 ? getFirstContentBetween(v[1], "''") : v[1]);
				} else if(currentLine.contains(getContainerClosure(false)))
					currentContainer = null;
				else
					throw new ScriptSyntaxException("invalid syntax '" + currentLine + "'", c);
			}
		}
		return builder.pack();
	}
	
	public LParser decompileAndParse(boolean replaceVariables) throws ScriptException {
		return new LParser(replaceVariables, this.decompile());
	}
	
	/**
	 * Resets the current compiling session.
	 * 
	 */
	private void resetSession() {
		this.kc = new AtomicInteger();
		this.vc = new AtomicInteger();
		this.ec = new AtomicInteger();
		this.cc = new AtomicInteger();
		this.cl = new AtomicInteger();
		LCompilerProperties.standardize(properties);
	}
	
	private String[] splitExact(String s, String splitreg) {
		if(splitreg.length() != 1)
			return new String[0];
		List<String> split = new ArrayList<String>();
		boolean open = false;
		String currentSplit = new String();
		StringArray array = ArrayUtils.splitAndModify(s);
		for(int i = 0; i < array.length(); i++) {
			if(array.get(i).equals(splitreg) && !open) {
				split.add(currentSplit);
				currentSplit = new String();
			} else if(array.get(i).equals("'")) {
				open = !open;
				currentSplit += array.get(i);
			} else
				currentSplit += array.get(i);
		}
		split.add(currentSplit);
		return split.toArray(new String[split.size()]);
	}
	
	/**
	 * 
	 * @param string
	 * @param regex
	 * @return the active split value of the current string
	 */
	private String getActiveSplitValue(String string, String regex) {
		String[] array = ArrayUtils.splitAndModify(string).toArray(),
				pattern = ArrayUtils.splitAndModify(regex).toArray();
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < pattern.length; j++) {
				if(array[i].equals(pattern[j])) {
					return pattern[j];
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param string
	 * @param regex
	 * @return if the string contains any value of the regex
	 */
	private boolean containsAny(String string, String regex) {
		return getActiveSplitValue(string, regex) != null;
	}
	
	/**
	 * 
	 * @param content
	 * @param regex
	 * @return the first content between the 2 symbols of regex
	 */
	private String getFirstContentBetween(String content, String regex) {
		Memorizer<Integer> open = new Memorizer<Integer>(0);
		StringArray array = ArrayUtils.splitAndModify(content),
				pattern = ArrayUtils.splitAndModify(regex);
		array.modifyEach(v -> {
			if(open.get() == 0 && v.equals(pattern.get(0))) {
				open.set(1);
				return "";
			} else if(open.get() == 1 && v.equals(pattern.get(1)))
				open.set(-1);
			return open.get() == 1 ? v : "";
		});
		return ArrayUtils.recreate(array.toArray());
	}
	
	/**
	 * 
	 * @param symbol
	 * @param string
	 * @return the amount of symbols the string contains
	 */
	private int getSymbolSize(char symbol, String string) {
		AtomicInteger i = new AtomicInteger();
		ArrayUtils.convertAndModify(string).forEach(v -> {
			if(v == symbol)
				i.getAndIncrement();
		});
		return i.get();
	}
	
	/**
	 * Deletes all useless spaces out of a string.
	 * 
	 * @param string
	 * @return a new spaceless string
	 */
	private String ignoreSpacesAndComments(String string) {
		Memorizer<Boolean> open = new Memorizer<Boolean>(false), commentOpen = new Memorizer<Boolean>(false);
		StringArray array = ArrayUtils.splitAndModify(string);
		array.modifyEach(v -> {
			if(v.equals("'"))
				open.set(!open.get());
			if(v.equals(getCommentBegin()))
				commentOpen.set(true);
			return (!open.get() && Character.isWhitespace(v.toCharArray()[0])) || commentOpen.get() ? "" : v;
		});
		return ArrayUtils.recreate(array.toArray());
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String getCommentBegin() {
		return (String) properties.get("comment.begin");
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String[] getComments() {
		String[] comments = (String[]) properties.get("comments");
		if(comments == null)
			return new String[0];
		else
			return comments;
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private int getKeyMin() {
		return (int) properties.get("keys.min");
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String getEntrySplit() {
		return String.valueOf(((String) properties.get("entry.split")));
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String getContainerOpening(boolean doubleBackSlash) {
		String s = (doubleBackSlash ? "\\" : "") + ((String) properties.get("container.opening"));
		try {
			s.split(s);
			return s;
		} catch(PatternSyntaxException e) {
			return (String) properties.get("container.opening");
		}
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String getContainerClosure(boolean doubleBackSlash) {
		String s = (doubleBackSlash ? "\\" : "") + ((String) properties.get("container.closure"));
		try {
			s.split(s);
			return s;
		} catch(PatternSyntaxException e) {
			return (String) properties.get("container.closure");
		}
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String getVariableSplit() {
		return String.valueOf(((String) properties.get("variable.split")));
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String convertValue(String value) {
		return value.split(" ").length > 1 ? " '" + value + "'" : " " + value;
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private String getKeyBegin() {
		return (String) properties.get("key.begin");
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean keyOverflow() {
		return overflow(limit("keys.limit"), kc);
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean commentOverflow() {
		return overflow(limit("comment.limit"), cl);
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean containerOverflow() {
		return overflow(limit("container.limit"), cc);
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean variableOverflow() {
		return overflow(limit("variable.limit"), vc);
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean entryOverflow() {
		return overflow(limit("entry.limit"), ec);
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean isValidContainerName(String name) {
		return !name.equals(name.replaceAll((String) properties.get("container.name.encoding"), ""));
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean isValidVariableName(String name) {
		return !name.equals(name.replaceAll((String) properties.get("variable.name.encoding"), ""));
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean isValidEntryName(String name) {
		return !name.equals(name.replaceAll((String) properties.get("entry.name.encoding"), ""));
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private boolean overflow(int lim, AtomicInteger c) {
		int x = c.incrementAndGet();
		if(lim < 0)
			return false;
		return x > lim;
	}
	
	/**
	 * 
	 * @return property helper
	 */
	private int limit(String key) {
		Object o = properties.get(key);
		if(o instanceof String && ((String) o).equals("unlimited"))
			return Integer.MIN_VALUE;
		return (int) o;
	}
	
	public static final class LCompilerProperties extends HashMap<String, Object> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Creates a new instance.
		 * 
		 */
		public LCompilerProperties() {
		}
		
		/**
		 * Sets the specified property key to the specified value.
		 * If the key is already set it gets overridden.
		 * 
		 * @param key
		 * @param value
		 * <p>
		 * <b><u>Properties:</u></b></p>
		 * <p><b>keys.limit</b> defines the limit of keywords in the script</p>
		 * <p><b>keys.min</b> defines the minimum amount of necessary keywords</p>
		 * <p><b>key.begin</b> defines the beginning character of each keyword</p>
		 * <p><b>comment.limit</b> defines the limit of comments in the script</p>
		 * <p><b>comment.begin</b> defines the beginning character of each comment</p>
		 * <p><b>comments</b> defines the comments as array</p>
		 * <p><b>variable.limit</b> defines the limit of variables in the script</p>
		 * <p><b>variable.name.encoding</b> defines the regex for valid name characters of variables</p>
		 * <p><b>variable.split</b> defines the possible character(s) for splitting the variables name and value</p>
		 * <p><b>container.limit</b> defines the limit of containers in the script</p>
		 * <p><b>container.name.encoding</b> defines the regex for valid name characters of containers</p>
		 * <p><b>container.opening</b> defines the beginning character of each container</p>
		 * <p><b>container.closure</b> defines the ending character of each container</p>
		 * <p><b>entry.limit</b> defines the limit of entries in each container in the script</p>
		 * <p><b>entry.name.encoding</b> defines the regex for valid name characters of entries</p>
		 * <p><b>entry.split</b> defines the possible character(s) for splitting the entries name and value</p>
		 */
		public void setProperty(String key, Object value) {
			if(containsKey(key)) {
				remove(key);
			}
			if(key != null && !key.isEmpty()) {
				put(key, value);
			}
		}
		
		/**
		 * Sets the specified property to the specified value only
		 * if it was not already set.
		 * 
		 * @param key
		 * @param value
		 * @see {@link LCompilerProperties#setProperty(String, Object)}
		 */
		public void setPropertySoftly(String key, Object value) {
			if(!containsKey(key)) {
				setProperty(key, value);
			}
		}
		
		/**
		 * 
		 * @param key the property key
		 * @return the property value of the specified key
		 */
		public Object getProperty(String key) {
			return get(key);
		}
		
		/**
		 * Sets all default properties to the specified property map softly.
		 * 
		 * @param properties the property map
		 * 
		 * <p>
		 * <b><u>Default values:</u></b></p>
		 * <p><b>keys.limit</b> 1</p>
		 * <p><b>keys.min</b> 1</p>
		 * <p><b>key.begin</b> '@'</p>
		 * <p><b>comment.limit</b> 'unlimited'</p>
		 * <p><b>comment.begin</b> '#'</p>
		 * <p><b>comments</b> null</p>
		 * <p><b>variable.limit</b> 'unlimited'</p>
		 * <p><b>variable.name.encoding</b> [A-z0-9_-]</p>
		 * <p><b>variable.split</b> ':'</p>
		 * <p><b>container.limit</b> 'unlimited'</p>
		 * <p><b>container.name.encoding</b> [A-z0-9_]</p>
		 * <p><b>container.opening</b> '{'</p>
		 * <p><b>container.closure</b> '}'</p>
		 * <p><b>entry.limit</b> 'unlimited'</p>
		 * <p><b>entry.name.encoding</b> [A-z0-9_-]</p>
		 * <p><b>entry.split</b> ':='</p>
		 */
		public static void standardize(LCompilerProperties properties) {
			properties.setPropertySoftly("keys.limit", 1);
			properties.setPropertySoftly("keys.min", 1);
			properties.setPropertySoftly("key.begin", "@");
			properties.setPropertySoftly("comment.limit", "unlimited");
			properties.setPropertySoftly("comment.begin", "#");
			properties.setPropertySoftly("comments", null);
			properties.setPropertySoftly("variable.limit", "unlimited");
			properties.setPropertySoftly("variable.name.encoding", "[A-z0-9_-]");
			properties.setPropertySoftly("variable.split", ":");
			properties.setPropertySoftly("container.limit", "unlimited");
			properties.setPropertySoftly("container.name.encoding", "[A-z0-9_]");
			properties.setPropertySoftly("container.opening", "{");
			properties.setPropertySoftly("container.closure", "}");
			properties.setPropertySoftly("entry.limit", "unlimited");
			properties.setPropertySoftly("entry.name.encoding", "[A-z0-9_-]");
			properties.setPropertySoftly("entry.split", ":=");
		}
		
	}
	
	public static class Session {
		
		/**
		 * Creates a new Session.
		 * 
		 */
		private Session() {
		}
		
		/**
		 * Creates a new compiling session for the specified content.
		 * 
		 * @param parsed the content
		 * @return the new session
		 */
		public static final Session createCompilingSession(LParser parsed) {
			return new CompilingSession(parsed);
		}
		
		/**
		 * Creates a new decompiling session for the specified raw content.
		 * 
		 * @param lines the raw content.
		 * @return the new session
		 */
		public static final Session createDecompilingSession(String[] lines) {
			return new DecompilingSession(lines);
		}
		
	}
	
	private static class CompilingSession extends Session {
		
		private LParser p;
		
		/**
		 * Creates a new compiling session with the specified parser.
		 * 
		 * @param p
		 */
		private CompilingSession(LParser p) {
			this.p = p;
		}
		
	}
	
	private static class DecompilingSession extends Session {
		
		private String[] lines;
		
		/**
		 * Creates a new decompiling session with the specified lines.
		 * 
		 * @param lines
		 */
		private DecompilingSession(String[] lines) {
			this.lines = lines;
		}
		
	}
	
}
