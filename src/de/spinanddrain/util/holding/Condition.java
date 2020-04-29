package de.spinanddrain.util.holding;

public interface Condition<T> {

	/**
	 * Defines a condition. Parameters can be used in combination with <b>de.spinanddrain.util.holding.Holder</b>.
	 * 
	 * @param current
	 * @param value
	 * @return  true or false
	 */
	boolean check(T current, T value);
	
}
