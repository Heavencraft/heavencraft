package fr.hc.rp.db.users;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.UserProvider;
import fr.hc.rp.HeavenRPInstance;

public class RPUserProvider extends UserProvider<RPUser>
{
	private RPUserProvider(ConnectionProvider connectionProvider)
	{
		super(connectionProvider, new RPUserFactory());
	}

	/*
	 * Singleton pattern
	 */

	private static RPUserProvider instance;

	public static RPUserProvider get()
	{
		if (instance == null)
			instance = new RPUserProvider(HeavenRPInstance.get().getConnectionProvider());

		return instance;
	}
}