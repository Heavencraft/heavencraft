package fr.hc.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.hc.core.db.homes.Home;
import fr.hc.core.exceptions.HeavenException;

public class ConversionUtil
{
	public static Location toLocation(Home home)
	{
		return new Location(Bukkit.getWorld(home.getWorld()), //
				home.getX(), home.getY(), home.getZ(), //
				home.getYaw(), home.getPitch());
	}

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
}