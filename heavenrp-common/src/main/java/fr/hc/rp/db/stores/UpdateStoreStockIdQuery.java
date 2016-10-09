package fr.hc.rp.db.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.db.stocks.Stock;

public class UpdateStoreStockIdQuery implements Query
{
	private static final String QUERY = "UPDATE stores SET stock_id = ? WHERE id = ? LIMIT 1;";

	private final Store store;
	private final Stock stock;
	private final StoreProvider provider;

	public UpdateStoreStockIdQuery(Store store, Stock stock, StoreProvider provider)
	{
		this.store = store;
		this.stock = stock;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (store.hasStockId() && store.getStockId() == stock.getId())
			return; // Nothing to do

		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, stock.getId());
			ps.setInt(2, store.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

			provider.invalidateCache(store);
		}
	}
}