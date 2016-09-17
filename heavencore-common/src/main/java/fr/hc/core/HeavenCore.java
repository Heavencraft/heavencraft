package fr.hc.core;

import fr.hc.core.connection.ConnectionProviderFactory;
import fr.hc.core.db.homes.HomeProvider;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.tasks.TaskManager;

public interface HeavenCore
{
	ConnectionProviderFactory getConnectionProviderFactory();

	HomeProvider getHomeProvider();

	UserProvider<? extends UserWithHome> getUserProvider();

	void setUserProvider(UserProvider<? extends UserWithHome> userProvider);

	TaskManager getTaskManager();
}