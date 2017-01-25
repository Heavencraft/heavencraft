package fr.hc.core.utils;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.core.db.homes.Home;

// Helper to convert Heavencraft types to Bukkit types
public class BukkitConversionUtil
{
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

	public static String toString(Collection<? extends Player> players)
	{
		final StringBuilder builder = LocalStringBuilder.get();

		final Iterator<? extends Player> it = players.iterator();
		while (it.hasNext())
		{
			builder.append(it.next().getName());
			if (it.hasNext())
				builder.append(ConversionUtil.SEPARATOR);
		}

		return LocalStringBuilder.release(builder);
	}
}