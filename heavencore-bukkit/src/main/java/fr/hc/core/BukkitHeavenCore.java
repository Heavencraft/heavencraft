package fr.hc.core;

import fr.hc.core.connection.ConnectionProviderFactory;
import fr.hc.core.connection.HikariConnectionProviderFactory;
import fr.hc.core.tasks.TaskManager;
import fr.hc.core.tasks.async.AsyncTaskExecutor;
import fr.hc.core.tasks.sync.BukkitSyncTaskExecutor;

public class BukkitHeavenCore extends AbstractBukkitPlugin implements HeavenCore
{
	private final ConnectionProviderFactory connectionProviderFactory = new HikariConnectionProviderFactory();
	private TaskManager taskManager;

	public BukkitHeavenCore()
	{
		HeavenCoreInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		taskManager = new TaskManager(new BukkitSyncTaskExecutor(), new AsyncTaskExecutor());
	}

	@Override
	public ConnectionProviderFactory getConnectionProviderFactory()
	{
		return connectionProviderFactory;
	}

	@Override
	public TaskManager getTaskManager()
	{
		return taskManager;
	}
}