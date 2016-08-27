package fr.hc.core;

import fr.hc.core.connection.ConnectionProviderFactory;
import fr.hc.core.connection.HikariConnectionProviderFactory;

public class BukkitHeavenCore extends AbstractBukkitPlugin implements HeavenCore
{
	private final ConnectionProviderFactory connectionProviderFactory = new HikariConnectionProviderFactory();

	public BukkitHeavenCore()
	{
		HeavenCoreInstance.set(this);
	}

	@Override
	public ConnectionProviderFactory getConnectionProviderFactory()
	{
		return connectionProviderFactory;
	}
}