package de.spinanddrain.util.operate;

public interface VoidOperation<T> {

	/**
	 * Performs an operation with a native value.
	 * 
	 * @param current  current native value
	 */
	void operate(T value);
	
}
