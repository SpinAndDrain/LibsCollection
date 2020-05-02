package de.spinanddrain.net.connection;

public interface Callback<A, B> {

	/**
	 * Executes a method with two parameters.
	 * 
	 * @param a first parameter
	 * @param b second parameter
	 */
	void respond(A a, B b);
	
}
