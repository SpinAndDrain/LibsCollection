package de.spinanddrain.lscript;

import de.spinanddrain.lscript.resources.LCompiler;
import de.spinanddrain.lscript.resources.LCompiler.LCompilerProperties;
import de.spinanddrain.lscript.resources.LCompiler.Session;
import de.spinanddrain.lscript.resources.LContainer;
import de.spinanddrain.lscript.resources.LScriptEntry;
import de.spinanddrain.lscript.tools.LReader.ScriptType;
import de.spinanddrain.util.arrays.ArrayUtils;

/**
 * 
 * LScript (Language Script) by SpinAndDrain version 1.0
 * 
 */
public class LScript {
	
	private static final LCompiler defaultCompiler = new LCompiler(null);
	
	/**
	 * Creates and returns a new LScriptEntry of <b>key</b> and <b>value</b>.
	 * 
	 * @param key
	 * @param value
	 * @return the new LScriptEntry
	 * @see {@link LContainer#subContainer(String, String)}
	 */
	public static LScriptEntry create(String key, String value) {
		return LContainer.subContainer(key, value).getContent()[0];
	}
	
	/**
	 * 
	 * @param name
	 * @return true if the specified variable <b>name</b> is valid
	 */
	public static boolean isValidVariableName(String name) {
		return name.length() > 0 && ArrayUtils.convertAndModify(name)
				.eliminate(v -> Character.isAlphabetic(v) || v == '_' || v == '-').isEmpty();
	}
	
	/**
	 * 
	 * @param name
	 * @return true if the specified container <b>name</b> is valid
	 */
	public static boolean isValidContainerName(String name) {
		return name.length() > 0 && ArrayUtils.convertAndModify(name)
				.eliminate(v -> Character.isAlphabetic(v) || v == '_' || Character.getNumericValue(v) >= 0).isEmpty();
	}
	
	/**
	 * 
	 * @param type the type of the script
	 * @param session compile or decompile
	 * @return the default <b>type</b> compiler of LScript
	 */
	public static LCompiler getDefaultCompilerFor(Session s, ScriptType type, String... comments) {
		LCompilerProperties props = new LCompilerProperties();
		if(type == ScriptType.TRANSLATION) {
			props.setProperty("keys.limit", 2);
			props.setProperty("keys.min", 2);
			if(comments != null && comments.length > 0)
				props.setProperty("comments", comments);
		}
		defaultCompiler.reInitSession(s, props);
		return defaultCompiler;
	}
	
}
