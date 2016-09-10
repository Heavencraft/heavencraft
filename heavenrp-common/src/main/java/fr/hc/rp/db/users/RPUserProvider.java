package fr.hc.rp.db.users;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.UserProvider;

public class RPUserProvider extends UserProvider<RPUser>
{
	public RPUserProvider(ConnectionProvider connectionProvider)
	{
		super(connectionProvider, new RPUserFactory());
	}
}