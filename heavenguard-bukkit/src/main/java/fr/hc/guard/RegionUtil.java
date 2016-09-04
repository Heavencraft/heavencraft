package fr.hc.guard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.guard.db.Flag;
import fr.hc.guard.db.regions.Region;

public class RegionUtil
{
	public static void loadState(Region region)
	{

	}

	public static void saveState(Region region) throws HeavenException
	{
		final World world = Bukkit.getWorld(region.getWorld());
		final Location pos1 = new Location(world, region.getMinX(), region.getMinY(), region.getMinZ());
		final Location pos2 = new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ());

		final byte[] schematic = WorldEditUtil.save(world, pos1, pos2);
		region.getFlagHandler().setByteArrayFlag(Flag.STATE, schematic);
	}
}
