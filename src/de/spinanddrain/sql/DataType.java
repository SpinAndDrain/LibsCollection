package de.spinanddrain.sql;

public enum DataType {

	CHAR(0, 255),
	VARCHAR(0, 255),
	TINYTEXT(0, 255),
	TEXT(0, 65535),
	BLOB(0, 65535),
	MEDIUMTEXT(0, 16777215),
	MEDIUMBLOB(0, 16777215),
	LONGTEXT(0, 4294967295L),
	LONGBLOB(0, 4294967295L),
	TINYINT(-128, 127),
	SMALLINT(-32768, 32767),
	MEDIUMINT(-8388608, 8388607),
	INT(-2147483648, 2147483647),
	BIGINT(-9223372036854775808L, 9223372036854775807L),
	FLOAT(0, 23),
	DOUBLE(24, 53),
	DECIMAL(24, 53),
	DATE(10, 10),
	DATETIME(18, 18),
	TIMESTAMP(14, 14),
	TIME(8, 8),
	ENUM(0, 0),
	SET(0, 0),
	BOOLEAN(0, 1);
	
	@Deprecated
	public final long MIN, MAX;
	
	/**
	 * MySQL datatypes.
	 * 
	 * @param min min length or range
	 * @param max max length or range
	 */
	private DataType(long min, long max) {
		this.MIN = min;
		this.MAX = max;
	}
	
}
