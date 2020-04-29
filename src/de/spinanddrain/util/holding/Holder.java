package de.spinanddrain.util.holding;

public class Holder<T> {

	private final Condition<T> condition;
	private T value;
	
	public Holder(Condition<T> condition) {
		this.condition = condition;
		this.value = null;
	}
	
	public Holder(Condition<T> condition, T beginValue) {
		this.condition = condition;
		this.value = beginValue;
	}
	
	/**
	 * Checks if the condition is true and overrides the current value with the new value if it is.
	 * The current value will be directly overridden if it is null.
	 * 
	 * @param condition
	 * @param value  new value
	 * @return  this
	 */
	public boolean check(T value) {
		if(this.value == null) {
			this.value = value;
			return true;
		}
		if(this.condition.check(this.value, value)) {
			this.value = value;
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the condition is true and overrides the current value with the new value if it is.
	 * The current value will be <b>not</b> directly overridden if it is null.
	 * 
	 * @param condition
	 * @param value  new value
	 * @return  this
	 * @throws NullPointerException
	 */
	public Holder<T> checkIgnoreNull(T value) throws NullPointerException {
		if(this.condition.check(this.value, value)) {
			this.value = value;
		}
		return this;
	}
	
	/**
	 * 
	 * @return  the current value
	 */
	public T get() {
		return value;
	}
	
}
