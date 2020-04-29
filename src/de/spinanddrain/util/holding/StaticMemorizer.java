package de.spinanddrain.util.holding;

public class StaticMemorizer<T> {

	private final T value;
	
	/**
	 * Stores a final value.
	 * 
	 * @param lastValue
	 */
	public StaticMemorizer(T lastValue) {
		this.value = lastValue;
	}

	/**
	 * 
	 * @return  the stored value of type T
	 */
	public T get() {
		return this.value;
	}
	
}
