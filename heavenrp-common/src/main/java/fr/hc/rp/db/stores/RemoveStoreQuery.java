package fr.hc.rp.db.stores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.async.AsyncTaskExecutor;
import fr.hc.core.tasks.queries.Query;

public class RemoveStoreQuery extends AsyncTaskExecutor implements Query
{
	private static final String QUERY = "DELETE FROM stores WHERE id = ? LIMIT 1;";

	private final Store store;
	private final StoreProvider provider;

	public RemoveStoreQuery(Store store, StoreProvider provider)
	{
		this.store = store;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, store.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

			provider.invalidateCache(store);
		}
	}
}