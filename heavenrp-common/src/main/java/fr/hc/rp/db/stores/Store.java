package fr.hc.rp.db.stores;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Store
{
	private final int id;
	private final int stockId;
	private final int quantity;
	private final int price;
	private final boolean isBuyer;
	private final String world;
	private final int x;
	private final int y;
	private final int z;

	Store(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.stockId = rs.getInt("stock_id");
		this.quantity = rs.getInt("qty");
		this.price = rs.getInt("price");
		this.isBuyer = rs.getBoolean("isBuyer");
		this.world = rs.getString("world");
		this.x = rs.getInt("x");
		this.y = rs.getInt("y");
		this.z = rs.getInt("z");
	}

	public int getId()
	{
		return id;
	}

	public int getQuantity()
	{
		return quantity;
	}

	/**
	 * Returns the id of the associated stock
	 * @return
	 */
	public int getStockId()
	{
		return stockId;
	}

	public int getPrice()
	{
		return price;
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

	/**
	 * Returns if the shop is buying this item
	 * @return
	 */
	public boolean isBuyer()
	{
		return isBuyer;
	}
	
}
