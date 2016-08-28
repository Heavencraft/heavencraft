package fr.hc.core;

import fr.hc.core.connection.ConnectionProviderFactory;
import fr.hc.core.tasks.TaskManager;

public interface HeavenCore
{
	ConnectionProviderFactory getConnectionProviderFactory();

	TaskManager getTaskManager();
}