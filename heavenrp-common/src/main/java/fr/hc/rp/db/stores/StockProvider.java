package fr.hc.rp.db.stores;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;

public class StockProvider
{
	private final HeavenRP plugin = HeavenRPInstance.get();
	
	public ConnectionProvider getConnectionProvider()
	{
		return plugin.getConnectionProvider();
	}
}
