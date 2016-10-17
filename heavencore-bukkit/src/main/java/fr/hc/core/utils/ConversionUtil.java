package fr.hc.core.utils;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.db.homes.Home;
import fr.hc.core.exceptions.HeavenException;

public class ConversionUtil
{
	private static final String SEPARATOR = ", ";

	public static final ThreadLocal<StringBuilder> localBuilder = new ThreadLocal<StringBuilder>()
	{
		@Override
		protected StringBuilder initialValue()
		{
			return new StringBuilder();
		}

		@Override
		public StringBuilder get()
		{
			final StringBuilder builder = super.get();
			builder.setLength(0);
			return builder;
		}
	};

	public static String toString(Collection<? extends Player> players)
	{
		final StringBuilder builder = localBuilder.get();

		final Iterator<? extends Player> it = players.iterator();
		while (it.hasNext())
		{
			builder.append(it.next().getName());
			if (it.hasNext())
				builder.append(SEPARATOR);
		}

		return builder.toString();
	}

	public static String toString(String[] array, int start, String separator)
	{
		final StringBuilder builder = localBuilder.get();

		for (int i = start; i != array.length; i++)
		{
			if (builder.length() != 0)
				builder.append(separator);
			builder.append(array[i]);
		}

		return builder.toString();
	}

	public static Location toLocation(Home home)
	{
		return new Location(Bukkit.getWorld(home.getWorld()), //
				home.getX(), home.getY(), home.getZ(), //
				home.getYaw(), home.getPitch());
	}

	public static Location toLocation(HeavenBlockLocation l)
	{
		return new Location(Bukkit.getWorld(l.getWorld()), l.getX(), l.getY(), l.getZ());
	}

	public static HeavenBlockLocation toHeavenBlockLocation(Location l)
	{
		return new HeavenBlockLocation(l.getWorld().getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
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