package fr.hc.core.db.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Warp
{
	
	private final int id;
	private final String name;
	private final String creator;
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	private final int price;
	
	
	protected Warp(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.name = rs.getString("name");
		this.creator = rs.getString("creator");
		this.world = rs.getString("world");
		this.x = rs.getDouble("x");
		this.y = rs.getDouble("y");
		this.z = rs.getDouble("z");
		this.yaw = rs.getFloat("yaw");
		this.pitch = rs.getFloat("pitch");
		this.price = rs.getInt("price");
	}
	

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getCreator()
	{
		return creator;
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

	public int getPrice()
	{
		return price;
	}

	public String getWorldName()
	{
		return world;
	}
}
