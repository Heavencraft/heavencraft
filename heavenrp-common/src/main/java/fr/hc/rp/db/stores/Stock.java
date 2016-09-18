package fr.hc.rp.db.stores;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock
{
	private final int id;
	private final int ownerId;
	private final int bankAccountId;
	private final String world;
	private final int x;
	private final int y;
	private final int z;

	Stock(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.ownerId = rs.getInt("owner_id");
		this.bankAccountId = rs.getInt("bank_account_id");
		this.world = rs.getString("world");
		this.x = rs.getInt("x");
		this.y = rs.getInt("y");
		this.z = rs.getInt("z");
	}

	public int getId()
	{
		return id;
	}

	public int getOwnerId()
	{
		return ownerId;
	}

	public int getBankAccountId()
	{
		return bankAccountId;
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
