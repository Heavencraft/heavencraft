package fr.hc.rp.db.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class CreateStoreQuery implements Query
{
	private static final String QUERY = "INSERT INTO stores(stock_id, qty, price, isBuyer, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

	private final StockProvider provider;
	private final User user;
	private final Stock stock;
	private final int quantity;
	private final int price;
	private final boolean isBuyer;
	private final String world;
	private final int x;
	private final int y;
	private final int z;

	public CreateStoreQuery(StockProvider provider, Stock stock, User builder, int price, int qty, boolean isBuyer,
			String world, int x, int y, int z)
	{
		this.user = builder;
		this.stock = stock;
		this.quantity = qty;
		this.price = price;
		this.isBuyer = isBuyer;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		// TODO check if builder is allowed to link the stock
		if(this.stock.getOwnerId() != this.user.getId())
			throw new HeavenException("Cet entrepot ne vous appartient pas.");
		
		
		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, this.stock.getId());
			ps.setInt(2, this.quantity);
			ps.setInt(3, this.price);
			ps.setBoolean(4, this.isBuyer);
			ps.setString(5, this.world);
			ps.setInt(6, x);
			ps.setInt(7, y);
			ps.setInt(8, z);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

		}
	}

}
