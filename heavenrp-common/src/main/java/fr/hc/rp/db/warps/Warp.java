package fr.hc.rp.db.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Warp
{
	private final int id;
	private final String name;
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	private final int price;
	private final int magicpoints;
	
	protected Warp(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.name = rs.getString("name");
		this.world = rs.getString("world");
		this.x = rs.getDouble("x");
		this.y = rs.getDouble("y");
		this.z = rs.getDouble("z");
		this.yaw = rs.getFloat("yaw");
		this.pitch = rs.getFloat("pitch");
		this.price = rs.getInt("price");
		this.magicpoints = rs.getInt("magicpoints");
	}

	public int getPrice()
	{
		return price;
	}

	public int getMagicpoints()
	{
		return magicpoints;
	}

	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getWorldName()
	{
		return world;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public double getZ()
	{
		return z;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getPitch()
	{
		return pitch;
	}
		
	
}
