package fr.hc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.utils.LocalStringBuilder;

public class HeavenBlockLocation
{
	private static final int HASHCODE_PRIME = 31;

	private final String world;
	private final int x;
	private final int y;
	private final int z;

	public HeavenBlockLocation(String world, int x, int y, int z)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public HeavenBlockLocation(String world, String x, String y, String z, ResultSet rs) throws SQLException
	{
		this.world = rs.getString(world);
		this.x = rs.getInt(x);
		this.y = rs.getInt(y);
		this.z = rs.getInt(z);
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = LocalStringBuilder.get();
		builder.append('(').append(world).append(", ").append(x).append(", ").append(y).append(", ").append(z).append(')');
		return LocalStringBuilder.release(builder);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof HeavenBlockLocation))
			return false;

		final HeavenBlockLocation other = (HeavenBlockLocation) obj;

		return other.x == x && other.y == y && other.z == z && (world != null ? world.equals(other.world) : other.world == null);
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = HASHCODE_PRIME * result + (world != null ? world.hashCode() : 0);
		result = HASHCODE_PRIME * result + x;
		result = HASHCODE_PRIME * result + y;
		result = HASHCODE_PRIME * result + z;
		return result;
	}

	public String getWorld()
	{
		return world;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}
}