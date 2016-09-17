package fr.hc.core;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.db.users.home.UserWithHome;

public interface ReferencePlugin
{
	ConnectionProvider getConnectionProvider();

	UserProvider<? extends UserWithHome> getUserProvider();
}