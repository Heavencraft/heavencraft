package fr.hc.rp;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.users.UsersListener;
import fr.hc.rp.db.users.RPUserProvider;

public class BukkitHeavenRP extends AbstractBukkitPlugin implements HeavenRP
{
	private ConnectionProvider connectionProvider;

	public BukkitHeavenRP()
	{
		HeavenRPInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		connectionProvider = createConnectionProvider(getConfig());

		new UsersListener(RPUserProvider.get());
	}

	@Override
	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}
}