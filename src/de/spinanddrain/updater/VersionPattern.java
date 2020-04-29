package de.spinanddrain.updater;

import de.spinanddrain.updater.exception.VersionParseException;
import de.spinanddrain.util.arrays.ArrayUtils;
import de.spinanddrain.util.arrays.IntArray;

public class VersionPattern {
	
	public static final int NEWER = 1, OLDER = -1, EQUAL = 0;
	
	private String version;
	
	/**
	 * Creates a new instance with the specified version string.
	 * 
	 * @param version
	 */
	public VersionPattern(String version) {
		this.version = version;
	}
	
	/**
	 * 
	 * @return the version string
	 */
	public String getVersionString() {
		return version;
	}
	
	/**
	 * 
	 * @param competitor
	 * @return true if <code>this</code> version string is newer then the competitor version string
	 */
	public boolean isNewerThan(String competitor) {
		return this.getRelationWith(competitor) == NEWER;
	}
	
	/**
	 * 
	 * @param competitor
	 * @return true if <code>this</code> version string is older then the competitor version string
	 */
	public boolean isOlderThan(String competitor) {
		return this.getRelationWith(competitor) == OLDER;
	}
	
	/**
	 * 
	 * @param competitor
	 * @return true if <code>this</code> and the competitor version strings are equal
	 */
	public boolean isEqual(String competitor) {
		return this.getRelationWith(competitor) == EQUAL;
	}
	
	/**
	 * 
	 * @param competitor
	 * @return the relation of <code>this</code> version string with the competitor version string
	 * @see VersionPattern#NEWER
	 * @see VersionPattern#OLDER
	 * @see VersionPattern#EQUAL
	 */
	public int getRelationWith(String competitor) {
		if(!isDefault(version) || !isDefault(competitor))
			throw new VersionParseException("cannot calculate the relation between non-default version strings");
		IntArray v = convert(version), c = convert(competitor);
		fillUp(v, c);
		for(int i = 0; i < v.length(); i++) {
			int a = v.get(i), b = c.get(i);
			if(a != b)
				return a > b ? 1 : -1;
		}
		return 0;
	}
	
	/**
	 * Fills up the array with the shorter length (a or b) with 0s.
	 * 
	 * @param a
	 * @param b
	 */
	public static void fillUp(IntArray a, IntArray b) {
		IntArray f = a.length() < b.length() ? a : b;
		while(a.length() != b.length())
			f.add(0);
	}
	
	/**
	 * Converts a version string into its numeric values.
	 * Example: 1.2.3 => [1, 2, 3]
	 * 
	 * @param version
	 * @return the new <code>IntArray</code> or throws a VersionParseException,
	 * 			if the version string not defaultly formatted
	 */
	public static IntArray convert(String version) {
		IntArray array = new IntArray();
		ArrayUtils.splitAndModify(version).eliminate(v -> v.equals(".")).forEach(v -> {
			try {
				array.add(Integer.parseInt(v));
			} catch(NumberFormatException e) {
				throw new VersionParseException("the version string is not convertable");
			}
		});
		return array;
	}
	
	/**
	 * Format: a.b.c...
	 * 
	 * @param versionString
	 * @return true if the specified version string has the "default" format
	 */
	public static boolean isDefault(String versionString) {
		return versionString.contains(".") && versionString.matches("[0-9]+(\\.[0-9]+)*");
	}
	
}
