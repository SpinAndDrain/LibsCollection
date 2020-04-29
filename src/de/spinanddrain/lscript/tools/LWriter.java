package de.spinanddrain.lscript.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.spinanddrain.lscript.LScript;
import de.spinanddrain.lscript.exception.FileNotSupportedException;
import de.spinanddrain.lscript.exception.ScriptException;
import de.spinanddrain.lscript.resources.LCompiler;
import de.spinanddrain.lscript.resources.LCompiler.Session;
import de.spinanddrain.lscript.resources.LContainer;
import de.spinanddrain.lscript.resources.LScriptEntry;
import de.spinanddrain.lscript.resources.ModifiableContainer;
import de.spinanddrain.lscript.resources.Variable;
import de.spinanddrain.lscript.tools.LParser.VirtualParsingSession;
import de.spinanddrain.lscript.tools.LReader.ScriptType;

public class LWriter {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private String versionType, patternType;
	private File file;
	private List<Variable> vars;
	private List<LContainer> trans;
	private List<String> comments;

	/**
	 * Creates a new instance with the specified file, version type and pattern
	 * type.
	 * 
	 * @param file
	 * @param versionType
	 * @param patternType
	 */
	public LWriter(File file, String versionType, String patternType) {
		this.versionType = versionType;
		this.patternType = patternType;
		this.file = file;
		this.vars = new ArrayList<>();
		this.trans = new ArrayList<>();
		this.comments = new ArrayList<>();
	}

	/**
	 * Adds a variable to this writer.
	 * 
	 * @param name
	 * @param value
	 * @return this
	 */
	public LWriter addVariable(String name, String value) {
		vars.add(Variable.of(name, value));
		return this;
	}

	/**
	 * Adds an array of containers to this writer.
	 * 
	 * @param container
	 * @return this
	 */
	public LWriter addTranslation(LContainer... containers) {
		for (LContainer c : containers) {
			trans.add(c);
		}
		return this;
	}

	/**
	 * Adds a non-builded container to this writer.
	 * 
	 * @param containerName
	 * @param entrys
	 * @return this
	 */
	public LWriter addTranslation(String containerName, LScriptEntry... entrys) {
		trans.add(new LContainer(containerName, entrys));
		return this;
	}

	/**
	 * Adds a comment to this writer. (Comments are placed at the beginning of the
	 * file)
	 * 
	 * @param comment
	 * @return this
	 */
	public LWriter addComment(String comment) {
		comments.add(comment);
		return this;
	}

	/**
	 * Writes the file based on the specified type and content.
	 * 
	 * @param type             the type of the file
	 * @param replaceVariables {@link VirtualParsingSession#VirtualParsingSession(String, String, Variable[], LContainer[], boolean)}
	 * @param method           the method for handling the process if the file
	 *                         already exists
	 * @throws FileNotSupportedException if the file has a invalid extension or is
	 *                                   not able to write
	 * @throws IOException               {@link FileWriter#FileWriter(File)}
	 * @throws ScriptException           while handling the existing content of the
	 *                                   file {@link LReader#read(ScriptType)}
	 */
	public void write(ScriptType type, OverridingMethod method, boolean replaceVariables)
			throws FileNotSupportedException, IOException, ScriptException {
		if (type == ScriptType.TRANSLATION)
			writeTranslation(method, replaceVariables);
		else if (type == ScriptType.PATTERN)
			writePattern(method, replaceVariables);
		else
			throw new NullPointerException();
	}

	/**
	 * Writes the file based on the specified type and content.
	 * <code>replaceVariables</code> is set to false. The
	 * <code>OverridingMethod</code> is set to <b>ALL</b>.
	 * 
	 * @param type the type of the file
	 * @throws FileNotSupportedException if the file has a invalid extension or is
	 *                                   not able to write
	 * @throws IOException               {@link FileWriter#FileWriter(File)}
	 * @throws ScriptException           while handling the existing content of the
	 *                                   file {@link LReader#read(ScriptType)}
	 */
	public void write(ScriptType type) throws FileNotSupportedException, IOException, ScriptException {
		this.write(type, OverridingMethod.ALL, false);
	}

	/**
	 * Write method for <code>ScriptType.TRANSLATION</code>
	 * 
	 * @throws FileNotSupportedException
	 * @throws IOException
	 * @throws ScriptException
	 */
	private void writeTranslation(OverridingMethod method, boolean replaceVariables)
			throws FileNotSupportedException, IOException, ScriptException {
		if (!file.getName().endsWith(".lang"))
			throw new FileNotSupportedException("File has invalid extension");
		if (!file.canWrite())
			throw new FileNotSupportedException("File is not writeable");
		if (file.length() != 0 && method == OverridingMethod.NONE)
			return;
		if (file.length() != 0 && method == OverridingMethod.UNEXISTING)
			this.restrict(new LReader(file).readAndParse(ScriptType.TRANSLATION));
		LCompiler compiler = LScript.getDefaultCompilerFor(
				Session.createCompilingSession(this.virtualizeThis(replaceVariables)), ScriptType.TRANSLATION,
				comments.toArray(new String[comments.size()]));
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (String line : compiler.compile()) {
			bw.write(line);
			bw.newLine();
		}
		bw.close();
	}

	/**
	 * Write method for <code>ScriptType.PATTERN</code>
	 * 
	 * @throws FileNotSupportedException
	 * @throws IOException
	 * @throws ScriptException
	 */
	private void writePattern(OverridingMethod method, boolean replaceVariables)
			throws FileNotSupportedException, IOException, ScriptException {
		if (!file.getName().endsWith(".ls"))
			throw new FileNotSupportedException("File has invalid extension");
		if (!file.canWrite())
			throw new FileNotSupportedException("File is not writeable");
		if (file.length() != 0 && method == OverridingMethod.NONE)
			return;
		if (file.length() != 0 && method == OverridingMethod.UNEXISTING)
			this.restrict(new LReader(file).readAndParse(ScriptType.PATTERN));
		LCompiler compiler = LScript.getDefaultCompilerFor(
				Session.createCompilingSession(this.virtualizeThis(replaceVariables)), ScriptType.PATTERN,
				comments.toArray(new String[comments.size()]));
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (String line : compiler.compile()) {
			bw.write(line);
			bw.newLine();
		}
		bw.close();
	}

	/**
	 * Restricts all values of this class for the specified existing file so that no
	 * already written content gets overridden.
	 * 
	 * @param p
	 */
	private void restrict(LParser p) {
		LParser c = this.virtualizeThis(false);
		Variable[] cv = c.getVariables();
		for (int i = 0; i < cv.length; i++) {
			for (Variable pv : p.getVariables()) {
				if (cv[i].getName().equals(pv.getName()))
					vars.set(i, Variable.of(cv[i].getName(), pv.getValue()));
			}
		}
		LContainer[] cc = c.getContent();
		for (int i = 0; i < cc.length; i++) {
			for (LContainer pc : p.getContent()) {
				if (cc[i].getName().equals(pc.getName())) {
					for (LScriptEntry ce : cc[i].getContent()) {
						for (LScriptEntry pe : pc.getContent()) {
							if (ce.getKey().equals(pe.getKey())) {
								ModifiableContainer m = cc[i].modify();
								m.set(ce.getKey(), pe.getValue());
								cc[i] = m;
							}
						}
					}
				}
			}
			trans.set(i, cc[i]);
		}
	}

	/**
	 * 
	 * @param replace
	 * @return a new virtual parsing session out of the current variables
	 */
	private LParser virtualizeThis(boolean replace) {
		return new VirtualParsingSession(versionType, patternType, vars.toArray(new Variable[vars.size()]),
				trans.toArray(new LContainer[trans.size()]), replace).virtualize();
	}

	public enum OverridingMethod {

		/**
		 * Overrides the the entire file.
		 */
		ALL,

		/**
		 * Overrides/Writes only the content which does not already exist. Already
		 * written content gets not modified.
		 */
		UNEXISTING,

		/**
		 * Overrides nothing (cancels the writing process if the file has any content).
		 */
		NONE;

	}

}
