package de.spinanddrain.util.advanced;

import java.util.Random;

import de.spinanddrain.util.arrays.ArrayUtils;
import de.spinanddrain.util.arrays.DoubleArray;
import de.spinanddrain.util.arrays.IntArray;
import de.spinanddrain.util.arrays.LongArray;
import de.spinanddrain.util.holding.Holder;
import de.spinanddrain.util.holding.Memorizer;

public final class MathUtils {

	private MathUtils() {
	}

	/**
	 * 
	 * @param num the number
	 * @return true if the number is even, false if not
	 */
	public static boolean isEven(long num) {
		return (num % 2 == 0);
	}

	/**
	 * 
	 * @param num the number
	 * @return true if the number is odd, false if not
	 */
	public static boolean isOdd(long num) {
		return (num % 2 == 1);
	}

	/**
	 * 
	 * @param num the number
	 * @return true if the number is prime, false if not
	 */
	public static boolean isPrime(long num) {
		for (int i = 2; i < num; i++) {
			if (num % i == 0)
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param max
	 * @param min
	 * @return a random int between the min and max value
	 */
	public static int random(int max, int min) {
		return new Random().nextInt((max - min) + 1) + min;
	}
	
	/**
	 * 
	 * @param nums elements
	 * @return highest value (n) of the elements (n > elements...)
	 */
	public static long getHighestOf(long... nums) {
		Holder<Long> h = new Holder<>((c, n) -> n > c);
		ArrayUtils.modify(nums).forEach(element -> h.check(element));
		return h.get();
	}

	/**
	 * 
	 * @param nums elements
	 * @return smallest value (n) of the elements (n < elements...)
	 */
	public static long getSmallestOf(long... nums) {
		Holder<Long> h = new Holder<>((c, n) -> n < c);
		ArrayUtils.modify(nums).forEach(element -> h.check(element));
		return h.get();
	}

	/**
	 * 
	 * @param nums elements
	 * @return highest value (n) of the elements (n > elements...)
	 */
	public static double getHighestOf(double... nums) {
		Holder<Double> h = new Holder<>((c, n) -> n > c);
		ArrayUtils.modify(nums).forEach(element -> h.check(element));
		return h.get();
	}

	/**
	 * 
	 * @param nums elements
	 * @return smallest value (n) of the elements (n < elements...)
	 */
	public static double getSmallestOf(double... nums) {
		Holder<Double> h = new Holder<>((c, n) -> n < c);
		ArrayUtils.modify(nums).forEach(element -> h.check(element));
		return h.get();
	}

	/**
	 * 
	 * @param arr  array of values
	 * @return  the sum of all values
	 */
	public static int sum(IntArray arr) {
		Memorizer<Integer> m = new Memorizer<Integer>(0);
		arr.forEach(element -> m.set(m.get() + element));
		return m.get();
	}
	
	/**
	 * 
	 * @param arr  array of values
	 * @return  the sum of all values
	 */
	public static int sum(int[] arr) {
		return sum(ArrayUtils.modify(arr));
	}
	
	/**
	 * 
	 * @param arr  array of values
	 * @return  the sum of all values
	 */
	public static long sum(LongArray arr) {
		Memorizer<Long> m = new Memorizer<Long>(0L);
		arr.forEach(element -> m.set(m.get() + element));
		return m.get();
	}
	
	/**
	 * 
	 * @param arr  array of values
	 * @return  the sum of all values
	 */
	public static long sum(long[] arr) {
		return sum(ArrayUtils.modify(arr));
	}

	/**
	 * 
	 * @param arr  array of values
	 * @return  the sum of all values
	 */
	public static double sum(DoubleArray arr) {
		Memorizer<Double> m = new Memorizer<Double>(0.0D);
		arr.forEach(element -> m.set(m.get() + element));
		return m.get();
	}
	
	/**
	 * 
	 * @param arr  array of values
	 * @return  the sum of all values
	 */
	public static double sum(double[] arr) {
		return sum(ArrayUtils.modify(arr));
	}
	
}
