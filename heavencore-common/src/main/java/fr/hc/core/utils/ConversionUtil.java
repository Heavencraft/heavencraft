package fr.hc.core.utils;

import fr.hc.core.exceptions.HeavenException;

public class ConversionUtil
{
	static final String SEPARATOR = ", ";

	public static String toString(String[] array, int start, String separator)
	{
		final StringBuilder builder = LocalStringBuilder.get();

		for (int i = start; i != array.length; i++)
		{
			if (builder.length() != 0)
				builder.append(separator);
			builder.append(array[i]);
		}

		return LocalStringBuilder.release(builder);
	}

	private static final int TINYINT_BYTES = 1;
	private static final int SMALLINT_BYTES = 2;
	private static final int MEDIUMINT_BYTES = 3;

	public static final int TINYINT_MAXVALUE = (1 << (TINYINT_BYTES * 8)) - 1;
	public static final int SMALLINT_MAXVALUE = (1 << (SMALLINT_BYTES * 8)) - 1;
	public static final int MEDIUMINT_MAXVALUE = (1 << (MEDIUMINT_BYTES * 8)) - 1;

	public static int toInt(String s) throws HeavenException
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch (final NumberFormatException ex)
		{
			throw new HeavenException("Le nombre {%1$s} est incorrect.", s);
		}
	}

	public static int toUint(String s) throws HeavenException
	{
		final int i = toInt(s);

		if (i < 0)
			throw new HeavenException("Le nombre {%1$s} est incorrect.", s);

		return i;
	}

	public static int toUint(String s, int maxValue) throws HeavenException
	{
		final int i = toUint(s);

		if (i > maxValue)
			throw new HeavenException("Le nombre {%1$s} est incorrect.", s);

		return i;
	}
}