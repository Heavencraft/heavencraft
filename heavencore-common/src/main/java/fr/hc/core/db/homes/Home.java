package fr.hc.core.db.homes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Home
{
	private final int userId;
	private final int homeNumber;
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float pitch;
	private final float yaw;

	protected Home(ResultSet rs) throws SQLException
	{
		userId = rs.getInt("user_id");
		homeNumber = rs.getInt("home_nb");
		world = rs.getString("world");
		x = rs.getDouble("x");
		y = rs.getDouble("y");
		z = rs.getDouble("z");
		pitch = rs.getFloat("pitch");
		yaw = rs.getFloat("yaw");
	}

	public int getUserId()
	{
		return userId;
	}

	public int getHomeNumber()
	{
		return homeNumber;
	}

	public String getWorld()
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

	public float getPitch()
	{
		return pitch;
	}

	public float getYaw()
	{
		return yaw;
	}
}