package fr.hc.guard.db;

import java.sql.Types;

public enum FlagType
{
	BOOLEAN(Types.BIT),
	BYTE_ARRAY(Types.LONGVARBINARY),
	TIMESTAMP(Types.TIMESTAMP);

	private final int sqlType;

	FlagType(int sqlType)
	{
		this.sqlType = sqlType;
	}

	public int getSQLType()
	{
		return sqlType;
	}
}