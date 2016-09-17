package fr.hc.guard;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.guard.db.RegionProvider;

public interface HeavenGuard
{
	ConnectionProvider getConnectionProvider();

	RegionProvider getRegionProvider();

	UserProvider<? extends User> getUserProvider();

	void setUserProvider(UserProvider<? extends User> userProvider);
}