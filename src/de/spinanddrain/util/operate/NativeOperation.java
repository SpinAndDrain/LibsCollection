package de.spinanddrain.util.operate;

public interface NativeOperation<T> {

	/**
	 * Performs an operation with a native value.
	 * 
	 * @param current  current native value
	 * @return  modified native value
	 */
	T operate(T current);
	
}
