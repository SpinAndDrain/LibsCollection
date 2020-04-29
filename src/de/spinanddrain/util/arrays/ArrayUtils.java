package de.spinanddrain.util.arrays;

import java.util.Arrays;

public final class ArrayUtils {

	private ArrayUtils() {
	}

	/**
	 * Creates a new <code>ObjectArray</code> instance with the specified content.
	 * 
	 * @param objects the content
	 * @return the new instance
	 */
	public static ObjectArray modify(Object[] objects) {
		if (objects == null || objects.length < 1) {
			return new ObjectArray();
		}
		return new ObjectArray(objects);
	}

	/**
	 * Creates a new <code>ByteArray</code> instance with the specified content.
	 * 
	 * @param bytes the content
	 * @return the new instance
	 */
	public static ByteArray modify(byte[] bytes) {
		if (bytes == null || bytes.length < 1) {
			return new ByteArray();
		}
		return new ByteArray(bytes);
	}

	/**
	 * Creates a new <code>ShortArray</code> instance with the specified content.
	 * 
	 * @param shorts the content
	 * @return the new instance
	 */
	public static ShortArray modify(short[] shorts) {
		if (shorts == null || shorts.length < 1) {
			return new ShortArray();
		}
		return new ShortArray(shorts);
	}

	/**
	 * Creates a new <code>IntArray</code> instance with the specified content.
	 * 
	 * @param integers the content
	 * @return the new instance
	 */
	public static IntArray modify(int[] integers) {
		if (integers == null || integers.length < 1) {
			return new IntArray();
		}
		return new IntArray(integers);
	}

	/**
	 * Creates a new <code>LongArray</code> instance with the specified content.
	 * 
	 * @param longs the content
	 * @return the new instance
	 */
	public static LongArray modify(long[] longs) {
		if (longs == null || longs.length < 1) {
			return new LongArray();
		}
		return new LongArray(longs);
	}

	/**
	 * Creates a new <code>FloatArray</code> instance with the specified content.
	 * 
	 * @param floats the content
	 * @return the new instance
	 */
	public static FloatArray modify(float[] floats) {
		if (floats == null || floats.length < 1) {
			return new FloatArray();
		}
		return new FloatArray(floats);
	}

	/**
	 * Creates a new <code>DoubleArray</code> instance with the specified content.
	 * 
	 * @param doubles the content
	 * @return the new instance
	 */
	public static DoubleArray modify(double[] doubles) {
		if (doubles == null || doubles.length < 1) {
			return new DoubleArray();
		}
		return new DoubleArray(doubles);
	}

	/**
	 * Creates a new <code>BooleanArray</code> instance with the specified content.
	 * 
	 * @param bools the content
	 * @return the new instance
	 */
	public static BooleanArray modify(boolean[] bools) {
		if (bools == null || bools.length < 1) {
			return new BooleanArray();
		}
		return new BooleanArray(bools);
	}

	/**
	 * Creates a new <code>CharArray</code> instance with the specified content.
	 * 
	 * @param chars the content
	 * @return the new instance
	 */
	public static CharArray modify(char[] chars) {
		if (chars == null || chars.length < 1) {
			return new CharArray();
		}
		return new CharArray(chars);
	}

	/**
	 * Creates a new <code>StringArray</code> instance with the specified content.
	 * 
	 * @param strings the content
	 * @return the new instance
	 */
	public static StringArray modify(String[] strings) {
		if (strings == null || strings.length < 1) {
			return new StringArray();
		}
		return new StringArray(strings);
	}

	/**
	 * Splits and converts the specified string into a <code>CharArray</code>.
	 * 
	 * @param string
	 * @return a new <code>CharArray</code> of each character of the string
	 */
	public static CharArray convertAndModify(String string) {
		if (string == null || string.isEmpty()) {
			return new CharArray();
		}
		return new CharArray(string.toCharArray());
	}

	/**
	 * Splits the specified string into a <code>StringArray</code>.
	 * 
	 * @param string
	 * @return a new <code>StringArray</code> of each character of the string
	 */
	public static StringArray splitAndModify(String string) {
		if (string == null) {
			return new StringArray();
		}
		String[] s = new String[string.length()];
		for (int i = 0; i < string.length(); i++) {
			s[i] = String.valueOf(string.charAt(i));
		}
		return new StringArray(s);
	}

	/**
	 * Tries to recreate a one-char <code>String[]</code> to a string.
	 * 
	 * @param chars
	 * @return the recreated <code>String[]</code> as String or an empty String if
	 *         the array is invalid
	 */
	public static String recreate(String[] chars) {
		String s = new String();
		if (chars == null || chars.length < 1) {
			return s;
		}
		chars = modify(chars).eliminate(v -> v.length() > 1).toArray();
		for (int i = 0; i < chars.length; i++) {
			s += chars[i];
		}
		return s;
	}

	/**
	 * 
	 * @param array
	 * @return the specified array to string
	 * @see {@link Arrays#toString(Object[]))}
	 */
	public static String toString(Array<?> array) {
		return Arrays.toString(array.toArray());
	}

}
