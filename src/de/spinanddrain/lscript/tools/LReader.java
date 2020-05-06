package de.spinanddrain.lscript.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.spinanddrain.lscript.LScript;
import de.spinanddrain.lscript.exception.FileNotSupportedException;
import de.spinanddrain.lscript.exception.ScriptException;
import de.spinanddrain.lscript.resources.LCompiler.Session;
import de.spinanddrain.lscript.resources.LContainer;

public class LReader {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private final File base;
	
	/**
	 * Creates a new instance with the specified file as base.
	 * 
	 * @param base
	 */
	public LReader(final File base) {
		this.base = base;
	}
	
	/**
	 * Reads a file by the specified type.
	 * 
	 * @param type type of the file
	 * @return the read raw content of the specified <b>base</b> file
	 * @throws FileNotSupportedException
	 * @throws ScriptException
	 * @throws IOException
	 */
	public LContainer[] read(ScriptType type) throws FileNotSupportedException, ScriptException, IOException {
		return LScript.getDefaultCompilerFor(Session.createDecompilingSession(this.read()), type).decompile();
	}
	
	/**
	 * Reads and parses a file by the specified type.
	 * Automatically replaces all variables.
	 * 
	 * @param type
	 * @return the parsed content of the specified <b>base</b> file
	 * @throws ScriptException
	 * @throws FileNotSupportedException
	 * @throws IOException 
	 */
	public LParser readAndParse(ScriptType type) throws ScriptException, FileNotSupportedException, IOException {
		return LScript.getDefaultCompilerFor(Session.createDecompilingSession(this.read()), type).decompileAndParse(true);
	}

	/**
	 * Reads and returns the basic content of a file.
	 * Does a few compatibility checks.
	 * 
	 * @return
	 * @throws FileNotSupportedException
	 * @throws IOException 
	 */
	private String[] read() throws FileNotSupportedException, IOException {
		if(base == null || !base.exists()) {
			throw new FileNotFoundException("File not found");
		}
		if(!base.getName().endsWith(".lang")) {
			throw new FileNotSupportedException("File has invalid extension");
		}
		if(!base.canRead()) {
			throw new FileNotSupportedException("File is not readable");
		}
		List<String> lines = new ArrayList<String>();
		BufferedReader s = new BufferedReader(new InputStreamReader(new FileInputStream(base), StandardCharsets.UTF_8));
		String line;
		while((line = s.readLine()) != null) {
			lines.add(line);
		}
		s.close();
		return lines.toArray(new String[lines.size()]);
	}
	
	public enum ScriptType {
		
		TRANSLATION,
		PATTERN;
		
	}
	
}
