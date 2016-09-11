package fr.hc.rp.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import fr.hc.core.db.warps.Warp;

public class WarpUtils
{

	/**
	 * Returns the bukkit world of a warp
	 * @param wr
	 * @return
	 */
	public static World getWorld(Warp wr)
	{
		return Bukkit.getWorld(wr.getWorldName());
	}
	
	/**
	 * Returns the location of a warp
	 * @param wr
	 * @return
	 */
	public static Location getLocation(Warp wr)
	{
		return new Location(WarpUtils.getWorld(wr), wr.getX(), wr.getY(), wr.getZ(), wr.getYaw(), wr.getPitch());
	}

}
