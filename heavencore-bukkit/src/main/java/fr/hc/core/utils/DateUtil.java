package fr.hc.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.hc.core.exceptions.HeavenException;

public class DateUtil
{
	private static final String DATETIME_FORMAT = "yyyyMMdd-HH:mm:ss";

	private static final DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);

	static
	{
		dateTimeFormat.setLenient(false);
	}

	public static Date parseDateTime(String value) throws HeavenException
	{
		try
		{
			return dateTimeFormat.parse(value);
		}
		catch (final ParseException ex)
		{
			throw new HeavenException("Le format de la date est incorrect. Utilisez {YYYYMMDD-HH:MM:SS}.");
		}
	}

	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static String toDateString(Date date)
	{
		return dateFormat.format(date);
	}

	public static boolean isToday(Date date)
	{
		if (date == null)
			return false;

		final Calendar calToday = Calendar.getInstance();
		final Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);

		return calToday.get(Calendar.ERA) == calDate.get(Calendar.ERA)
				&& calToday.get(Calendar.YEAR) == calDate.get(Calendar.YEAR)
				&& calToday.get(Calendar.DAY_OF_YEAR) == calDate.get(Calendar.DAY_OF_YEAR);
	}
}