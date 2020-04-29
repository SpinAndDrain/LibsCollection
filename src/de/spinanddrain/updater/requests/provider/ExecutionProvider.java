package de.spinanddrain.updater.requests.provider;

import java.io.File;

public interface ExecutionProvider {
	
	/**
	 * This method gets called before the new file
	 * is downloaded.
	 * 
	 * @return the new file name
	 */
	String preparation();
	
	/**
	 * This method gets called after the new file
	 * is downloaded.
	 * 
	 * @param f the downloaded result
	 */
	void postProcessing(File f);
	
}
