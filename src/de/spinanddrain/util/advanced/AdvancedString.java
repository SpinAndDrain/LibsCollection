package de.spinanddrain.util.advanced;

import de.spinanddrain.util.Utils;
import de.spinanddrain.util.arrays.ArrayUtils;
import de.spinanddrain.util.holding.Holder;
import de.spinanddrain.util.operate.NativeOperation;

public class AdvancedString {

	private String base;
	
	/**
	 * Creates a new instance with an empty string as base.
	 * 
	 */
	public AdvancedString() {
		this.base = new String();
	}
	
	/**
	 * Creates a new instance with the specified string as base.
	 * 
	 * @param base
	 */
	public AdvancedString(String base) {
		this.base = base;
	}
	
	@Override
	public String toString() {
		return this.base;
	}
	
	/**
	 * Performs an native operation to the current String.
	 * 
	 * @param operation
	 * @return  this
	 */
	public AdvancedString operate(NativeOperation<String> operation) {
		this.base = operation.operate(this.base);
		return this;
	}
	
	/**
	 * Checks the String for all single characters of {pattern}.
	 * 
	 * @param pattern  all characters
	 * @return  true if the String contains any of the single characters, false if not
	 */
	public boolean containsAny(String pattern) {
		String[] regex = ArrayUtils.splitAndModify(pattern).toArray();
		for(int i = 0; i < regex.length; i++) {
			if(this.base.contains(regex[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks the String for all single characters of {pattern}.
	 * 
	 * @param pattern  all characters
	 * @return  true if the String contains all of the single characters at least once, false if not
	 */
	public boolean containsAll(String pattern) {
		String[] regex = ArrayUtils.splitAndModify(pattern).toArray();
		for(int i = 0; i < regex.length; i++) {
			if(!this.base.contains(regex[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks the start of the String for all single characters of {pattern}.
	 * 
	 * @param pattern  all characters
	 * @return  true if the String starts with any of the single characters, false if not
	 */
	public boolean startsWithAny(String pattern) {
		String[] regex = ArrayUtils.splitAndModify(pattern).toArray();
		for(int i = 0; i < regex.length; i++) {
			if(this.base.startsWith(regex[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks the end of the String for all single characters of {pattern}.
	 * 
	 * @param pattern  all characters
	 * @return  true if the String ends with any of the single characters, false if not
	 */
	public boolean endsWithAny(String pattern) {
		String[] regex = ArrayUtils.splitAndModify(pattern).toArray();
		for(int i = 0; i < regex.length; i++) {
			if(this.base.endsWith(regex[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Capitalizes all characters of the given amount at the start of the String.
	 * 
	 * @param amount  n
	 * @return  this
	 */
	public AdvancedString capitalize(int amount) {
		if(this.base.length() <= amount) {
			this.base = this.base.toUpperCase();
			return this;
		}
		this.base = this.base.substring(0, amount).toUpperCase() + this.base.substring(amount);
		return this;
	}
	
	/**
	 * Capitalizes all characters of the given amount at the end of the String.
	 * 
	 * @param amount  n
	 * @return  this
	 */
	public AdvancedString pushUp(int amount) {
		if(this.base.length() <= amount) {
			this.base = this.base.toUpperCase();
			return this;
		}
		int beginIndex = this.base.length() - amount;
		this.base = this.base.substring(0, beginIndex) + this.base.substring(beginIndex).toUpperCase();
		return this;
	}
	
	/**
	 * Writes all characters of the given amount at the start of the String in lower case.
	 * 
	 * @param amount  n
	 * @return  this
	 */
	public AdvancedString lowercase(int amount) {
		if(this.base.length() <= amount) {
			this.base = this.base.toLowerCase();
			return this;
		}
		this.base = this.base.substring(0, amount).toLowerCase() + this.base.substring(amount);
		return this;
	}
	
	/**
	 * Writes all characters of the given amount at the end of the String in lower case.
	 * 
	 * @param amount  n
	 * @return  this
	 */
	public AdvancedString pushDown(int amount) {
		if(this.base.length() <= amount) {
			this.base = this.base.toLowerCase();
			return this;
		}
		int beginIndex = this.base.length() - amount;
		this.base = this.base.substring(0, beginIndex) + this.base.substring(beginIndex).toLowerCase();
		return this;
	}
	
	/**
	 * Removes all superfluous characters from this String.
	 * Example: "Heeeey" -> "Hey"
	 * 
	 * @param ignorePattern  pattern of all characters that should be ignored
	 * @return  this
	 */
	public AdvancedString removeSuperfluousChars(String ignorePattern) {
		Holder<String> hold = new Holder<String>((c, n) -> !n.equalsIgnoreCase(c));
		this.base = ArrayUtils.recreate(ArrayUtils.splitAndModify(this.base).eliminate(v ->
			!Utils.expand(v.toLowerCase()).containsAny(ignorePattern.toLowerCase()) && !hold.check(v)).toArray());
		return this;
	}
	
	/**
	 * Removes all superfluous characters from this String without ignoring any characters.
	 * 
	 * @return  this
	 */
	public AdvancedString removeSuperfluousChars() {
		this.removeSuperfluousChars(new String());
		return this;
	}
	
	/**
	 * Binds and converts an array of Strings into one single String and separates each value with a link.
	 * 
	 * @param link  the String which is put in between each value of the array
	 * @param array  the values that should be bind
	 * @return  the new binded String (throws a NullPointerException if the link is null)
	 */
	public static String bind(String link, String[] array) {
		if(array == null) {
			return null;
		}
		if(link == null) {
			throw new NullPointerException();
		}
		return new AdvancedString(ArrayUtils.recreate(ArrayUtils.splitAndModify(ArrayUtils.toString(ArrayUtils.modify(array)))
				.pop(1).shift(1).toArray())).operate(value -> value.replace(", ", link)).toString();
	}
	
}
