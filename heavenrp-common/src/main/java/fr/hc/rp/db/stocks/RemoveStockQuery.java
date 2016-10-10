package fr.hc.rp.db.stocks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.async.AsyncTaskExecutor;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.HeavenRPInstance;

public class RemoveStockQuery extends AsyncTaskExecutor implements Query
{
	private static final String QUERY = "DELETE FROM stocks WHERE id = ? LIMIT 1;";

	private final Stock stock;
	private final StockProvider provider;

	public RemoveStockQuery(Stock stock, StockProvider provider)
	{
		this.stock = stock;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, stock.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

			provider.invalidateCache(stock);
			HeavenRPInstance.get().getStoreProvider().invalidateCache(stock);
		}
	}
}