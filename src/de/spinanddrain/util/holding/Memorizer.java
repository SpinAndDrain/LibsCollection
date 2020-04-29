package de.spinanddrain.util.holding;

public class Memorizer<T> {

	private T value;
	
	/**
	 * Stores null.
	 * 
	 */
	public Memorizer() {
		this.value = null;
	}
	
	/**
	 * Stores the specified value.
	 * 
	 * @param value
	 */
	public Memorizer(T value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return  the stored value of type T
	 */
	public T get() {
		return this.value;
	}

	/**
	 * Updates the value.
	 * 
	 * @param newValue
	 */
	public void set(T newValue) {
		this.value = newValue;
	}
	
}
