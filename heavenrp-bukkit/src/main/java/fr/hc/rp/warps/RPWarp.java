package fr.hc.rp.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import fr.hc.rp.db.warps.Warp;

public class RPWarp extends Warp
{
	
	protected RPWarp(ResultSet rs) throws SQLException
	{
		super(rs);
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(super.getWorldName());
	}
	
	public Location getLocation()
	{
		return new Location(getWorld(), this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
	}

}
