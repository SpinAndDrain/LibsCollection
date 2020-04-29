package de.spinanddrain.lscript.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	 * @throws FileNotFoundException
	 * @throws ScriptException
	 */
	public LContainer[] read(ScriptType type) throws FileNotSupportedException, FileNotFoundException, ScriptException {
		return LScript.getDefaultCompilerFor(Session.createDecompilingSession(this.read()), type).decompile();
	}
	
	/**
	 * Reads and parses a file by the specified type.
	 * Automatically replaces all variables.
	 * 
	 * @param type
	 * @return the parsed content of the specified <b>base</b> file
	 * @throws FileNotFoundException
	 * @throws ScriptException
	 * @throws FileNotSupportedException
	 */
	public LParser readAndParse(ScriptType type) throws FileNotFoundException, ScriptException, FileNotSupportedException {
		return LScript.getDefaultCompilerFor(Session.createDecompilingSession(this.read()), type).decompileAndParse(true);
	}

	/**
	 * Reads and returns the basic content of a file.
	 * Does a few compatibility checks.
	 * 
	 * @return
	 * @throws FileNotSupportedException
	 * @throws FileNotFoundException
	 */
	private String[] read() throws FileNotSupportedException, FileNotFoundException {
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
		Scanner s = new Scanner(base);
		while(s.hasNextLine()) {
			lines.add(s.nextLine());
		}
		s.close();
		return lines.toArray(new String[lines.size()]);
	}
	
	public enum ScriptType {
		
		TRANSLATION,
		PATTERN;
		
	}
	
}
