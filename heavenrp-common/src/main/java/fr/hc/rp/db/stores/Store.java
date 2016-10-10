package fr.hc.rp.db.stores;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.HeavenBlockLocation;
import fr.hc.rp.db.stocks.CompanyIdAndStockName;
import fr.hc.rp.db.stocks.Stock;

public class Store
{
	private final int id;

	private final boolean hasStockId;
	private final int stockId;
	private final CompanyIdAndStockName companyIdAndStockName;

	private final int quantity;
	private final int price;
	private final boolean isBuyer;

	private final HeavenBlockLocation location;

	// Available from package only
	Store(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt("id");
		this.stockId = rs.getInt("stock_id");
		this.hasStockId = !rs.wasNull();
		this.companyIdAndStockName = new CompanyIdAndStockName("company_id", "stock_name", rs);
		this.quantity = rs.getInt("quantity");
		this.price = rs.getInt("price");
		this.isBuyer = rs.getBoolean("is_buyer");

		this.location = new HeavenBlockLocation("world", "x", "y", "z", rs);
	}

	// Available from package only
	Store(int id, Stock stock, CompanyIdAndStockName companyIdAndStockName, int quantity, int price, boolean isBuyer,
			HeavenBlockLocation location)
	{
		this.id = id;
		this.stockId = stock != null ? stock.getId() : 0;
		this.hasStockId = stock != null;
		this.companyIdAndStockName = companyIdAndStockName;
		this.quantity = quantity;
		this.price = price;
		this.isBuyer = isBuyer;
		this.location = location;
	}

	@Override
	public String toString()
	{
		return companyIdAndStockName.toString();
	}

	public int getId()
	{
		return id;
	}

	public CompanyIdAndStockName getCompanyIdAndStockName()
	{
		return companyIdAndStockName;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public boolean hasStockId()
	{
		return hasStockId;
	}

	/**
	 * Returns the id of the associated stock
	 * 
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

	public HeavenBlockLocation getLocation()
	{
		return location;
	}

	/**
	 * Returns if the shop is buying this item
	 * 
	 * @return
	 */
	public boolean isBuyer()
	{
		return isBuyer;
	}
}