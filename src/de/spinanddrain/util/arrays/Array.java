package de.spinanddrain.util.arrays;

import java.util.List;

import de.spinanddrain.util.operate.NativeOperation;
import de.spinanddrain.util.operate.VoidOperation;

public interface Array<T> {

	/**
	 * Adds an element to the packet
	 * 
	 * @param object  the element
	 * @return  this
	 */
	Array<T> add(T object);
	
	/**
	 * Removes an element at the {index} position of the packet.
	 * 
	 * @param index
	 * @return  this
	 */
	Array<T> remove(int index);
	
	/**
	 * Removes {amount} elements at the front of the packet.
	 * 
	 * @param amount
	 * @return  this
	 */
	Array<T> shift(int amount);
	
	/**
	 * Adds an element at the front of the packet.
	 * 
	 * @param object  the element
	 * @return  this
	 */
	Array<T> unshift(T object);
	
	/**
	 * Removes {amount} elements at the end of the packet.
	 * 
	 * @param amount
	 * @return  this
	 */
	Array<T> pop(int amount);
	
	/**
	 * Changes the position of the element at the position {s} with the element at the position {t}
	 * 
	 * @param s
	 * @param t
	 * @return  this
	 */
	Array<T> move(int s, int t);
	
	/**
	 * Adds multiple {content} to the packet.
	 * 
	 * @param content
	 * @return  this
	 */
	Array<T> fill(T[] content);
	
	/**
	 * Overrides and filles the packet with the {content}.
	 * 
	 * @param content
	 * @return  this
	 */
	Array<T> override(T[] content);
	
	/**
	 * Rotates in positive (right) direction.
	 * 
	 * @param count  number of rotations
	 * @return  this
	 */
	Array<T> push(int count);
	
	/**
	 * Rotates in negative (left) direction.
	 * 
	 * @param count  number of rotations
	 * @return  this
	 */
	Array<T> pull(int count);
	
	/**
	 * Keeps all values that are not filtered out by the filter.
	 * 
	 * @param filter
	 * @return  this
	 */
	Array<T> keep(Filter<T> filter);
	
	/**
	 * Keeps all values that are filtered out by the filter.
	 * 
	 * @param filter
	 * @return
	 */
	Array<T> eliminate(Filter<T> filter);
	
	/**
	 * Clears the array.
	 * 
	 * @return  this
	 */
	Array<T> clear();
	
	/**
	 * Clears the array and creates a empty array with the specified length.
	 * 
	 * @param newLength  length of the new array
	 * @return  this
	 */
	Array<T> clear(int newLength);
	
	/**
	 * Returns the first matching object with the {object}.
	 * 
	 * @param object
	 * @return  the index of the first matching object, -1 if no matching object was found
	 */
	int firstIndexOf(T object);
	
	/**
	 * Returns the last matching object with the {object}.
	 * 
	 * @param object
	 * @return  the index of the last matching object, -1 if no matching object was found
	 */
	int lastIndexOf(T object);
	
	/**
	 * Checks if the array is equal to another array.
	 * 
	 * @param other  the other array
	 * @return  true if both arrays matches, false if not
	 */
	boolean matches(T[] other);
	
	/**
	 * 
	 * @return  true if the array is empty, false if not
	 */
	boolean isEmpty();
	
	/**
	 * Executes an operation for each value of the array.
	 * 
	 * @param operate  operation
	 * @return  this
	 */
	Array<T> modifyEach(NativeOperation<T> operate);
	
	/**
	 * Executes an operation without return for each value of the array.
	 * 
	 * @param operate
	 * @return  this
	 */
	Array<T> forEach(VoidOperation<T> operate);
	
	/**
	 * Splits the array at the specified index and returns the new subarray.
	 * 
	 * @param splitIndex  index where the array should be split
	 * @return  the new subarray
	 */
	Array<T> subarray(int splitIndex);
	
	/**
	 * Splits the array at the specified begin index and returns all values until the end index is reached in a new array.
	 * 
	 * @param splitIndex  index where the array should be split
	 * @return  the new subarray
	 */
	Array<T> subarray(int beginIndex, int endIndex);
	
	/**
	 * Creates a copy of the current array.
	 * 
	 * @return  a new copy of the current instance
	 */
	Array<T> copy();
	
	/**
	 * Inserts an object at the specified index.
	 * 
	 * @param object
	 * @param index
	 * @return  this
	 */
	Array<T> insert(T object, int index);
	
	/**
	 * 
	 * @return  current length of the array
	 */
	int length();
	
	/**
	 * Reverses the array. ({1, 2, 3} -> {3, 2, 1})
	 * 
	 * @return  this
	 */
	Array<T> reverse();
	
	/**
	 * Shuffles the array randomly.
	 * 
	 * @return this
	 */
	Array<T> shuffle();
	
	/**
	 * Sorts the array by ordering the primitive types.
	 * (Does not work for non-primitive types)
	 * 
	 * @return this
	 */
	Array<T> sort();
	
	/**
	 * 
	 * @param index
	 * @return  the value of the array at the specified index
	 */
	T get(int index);
	
	/**
	 * 
	 * @return  the (modified) array
	 */
	T[] toArray();
	
	/**
	 * 
	 * @return  the (modified) array as <b>java.util.List</b>
	 */
	List<T> toList();
	
}
