package fr.hc.rp;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.users.UsersListener;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.rp.db.users.RPUser;
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

		final UserProvider<RPUser> userProvider = RPUserProvider.get();
		HeavenGuardInstance.get().setUserProvider(userProvider);
		new UsersListener(userProvider);
	}

	@Override
	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}
}