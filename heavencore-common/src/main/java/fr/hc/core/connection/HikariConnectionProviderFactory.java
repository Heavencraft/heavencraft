package fr.hc.core.connection;

import java.util.HashMap;
import java.util.Map;

public class HikariConnectionProviderFactory implements ConnectionProviderFactory
{
	private final Map<String, ConnectionProvider> providersByDatabase = new HashMap<String, ConnectionProvider>();

	@Override
	public ConnectionProvider newConnectionProvider(String database, String username, String password)
	{
		ConnectionProvider provider = providersByDatabase.get(database);

		if (provider == null)
			providersByDatabase.put(database, provider = new HikariConnectionProvider(database, username, password));

		return provider;
	}
}