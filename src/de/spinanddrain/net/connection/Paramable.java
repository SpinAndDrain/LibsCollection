package de.spinanddrain.net.connection;

public interface Paramable<T> {

	/**
	 * Executes a method with one parameter.
	 * 
	 * @param param the parameter
	 */
	void run(T param);
	
}
