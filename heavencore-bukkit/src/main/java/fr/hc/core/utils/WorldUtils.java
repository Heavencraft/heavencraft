package fr.hc.core.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WorldUtils
{
	// Return the safe location, or null if no location is safe
	public static Location getSafeLocation(World world, int x, int z)
	{
		final int y = world.getHighestBlockYAt(x, z);

		if (isBlockUnsafe(world, x, y, z))
			return null;

		return new Location(world, x, y, z);
	}

	public static Location getSafeDestination(World world, int x, int z)
	{
		final int y = world.getHighestBlockYAt(x, z);

		if (isBlockUnsafe(world, x, y, z))
			return getSafeDestination(world, x + 1, z);

		return new Location(world, x + 0.5D, y, z + 0.5D);
	}

	private static boolean isBlockUnsafe(World world, int x, int y, int z)
	{
		if (world.getBlockAt(x, y, z).getType() != Material.AIR
				|| world.getBlockAt(x, y + 1, z).getType() != Material.AIR)
		{
			return true;
		}

		final Block below = world.getBlockAt(x, y - 1, z);

		switch (below.getType())
		{
			case LAVA:
			case STATIONARY_LAVA:
			case FIRE:
				return true;
			default:
				return false;
		}
	}
}
