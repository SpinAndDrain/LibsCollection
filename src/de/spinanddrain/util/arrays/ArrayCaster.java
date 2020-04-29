package de.spinanddrain.util.arrays;

public class ArrayCaster {
	
	private ArrayCaster() {
	}
	
	// Functions to cast types to byte
	public static byte[] convertToByte(short... array) {
		byte[] casted = new byte[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (byte) array[i];
		return casted;
	}
	
	public static byte[] convertToByte(int... array) {
		byte[] casted = new byte[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (byte) array[i];
		return casted;
	}
	
	public static byte[] convertToByte(long... array) {
		byte[] casted = new byte[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (byte) array[i];
		return casted;
	}
	
	// Functions to cast to short
	public static short[] convertToShort(byte... array) {
		short[] casted = new short[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (short) array[i];
		return casted;
	}
	
	public static short[] convertToShort(int... array) {
		short[] casted = new short[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (short) array[i];
		return casted;
	}
	
	public static short[] convertToShort(long... array) {
		short[] casted = new short[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (short) array[i];
		return casted;
	}
	
	// Functions to cast to int
	public static int[] convertToInt(byte... array) {
		int[] casted = new int[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (int) array[i];
		return casted;
	}
	
	public static int[] convertToInt(short... array) {
		int[] casted = new int[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (int) array[i];
		return casted;
	}
	
	public static int[] convertToInt(long... array) {
		int[] casted = new int[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (int) array[i];
		return casted;
	}
	
	// Functions to cast to long
	public static long[] convertToLong(byte... array) {
		long[] casted = new long[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (long) array[i];
		return casted;
	}
	
	public static long[] convertToLong(short... array) {
		long[] casted = new long[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (long) array[i];
		return casted;
	}
	
	public static long[] convertToLong(int... array) {
		long[] casted = new long[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (long) array[i];
		return casted;
	}
	
	// Functions to cast float & double
	public static float[] convertToFloat(double... array) {
		float[] casted = new float[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (float) array[i];
		return casted;
	}
	
	public static double[] convertToDouble(float... array) {
		double[] casted = new double[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (double) array[i];
		return casted;
	}
	
	// Functions to cast between Integers and Decimal numbers
	public static int[] convertToInt(float... array) {
		int[] casted = new int[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (int) array[i];
		return casted;
	}
	
	public static int[] convertToInt(double... array) {
		int[] casted = new int[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (int) array[i];
		return casted;
	}
	
	public static float[] convertToFloat(int... array) {
		float[] casted = new float[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (float) array[i];
		return casted;
	}
	
	public static double[] convertToDouble(int... array) {
		double[] casted = new double[array.length];
		for(int i = 0; i < casted.length; i++)
			casted[i] = (double) array[i];
		return casted;
	}
	
	// Functions to cast native types to types <=>
	public static byte[] cast(Byte... array) {
		return new ByteArray(array).toNativeArray();
	}
	
	public static short[] cast(Short... array) {
		return new ShortArray(array).toNativeArray();
	}
	
	public static int[] cast(Integer... array) {
		return new IntArray(array).toNativeArray();
	}
	
	public static long[] cast(Long... array) {
		return new LongArray(array).toNativeArray();
	}
	
	public static double[] cast(Double... array) {
		return new DoubleArray(array).toNativeArray();
	}
	
	public static float[] cast(Float... array) {
		return new FloatArray(array).toNativeArray();
	}
	
	public static boolean[] cast(Boolean... array) {
		return new BooleanArray(array).toNativeArray();
	}
	
	public static char[] cast(Character... array) {
		return new CharArray(array).toNativeArray();
	}
	
	public static Byte[] cast(byte... array) {
		return new ByteArray(array).toArray();
	}

	public static Short[] cast(short... array) {
		return new ShortArray(array).toArray();
	}
	
	public static Integer[] cast(int... array) {
		return new IntArray(array).toArray();
	}
	
	public static Long[] cast(long... array) {
		return new LongArray(array).toArray();
	}
	
	public static Double[] cast(double... array) {
		return new DoubleArray(array).toArray();
	}
	
	public static Float[] cast(float... array) {
		return new FloatArray(array).toArray();
	}
	
	public static Boolean[] cast(boolean... array) {
		return new BooleanArray(array).toArray();
	}
	
	public static Character[] cast(char... array) {
		return new CharArray(array).toArray();
	}
	
}
