package de.spinanddrain.util.arrays;

public interface Filter<T> {

	/**
	 * Checks if the given value should be kept in the array or not.
	 * 
	 * @param value  object to be checked
	 * @return  <b>boolean</b> true if the object is kept, false if not
	 */
	boolean keep(T value);
	
	/**
	 * Returns the negated filter of this filter.
	 * 
	 * @return
	 */
	default Filter<T> negate() {
		return (value) -> !keep(value);
	}
	
}
