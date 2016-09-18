package fr.hc.core;

import fr.hc.core.cmd.admin.CreacheatCommand;
import fr.hc.core.cmd.admin.EndercheatCommand;
import fr.hc.core.cmd.admin.HealCommand;
import fr.hc.core.cmd.admin.InventoryCommand;
import fr.hc.core.cmd.admin.RoucoupsCommand;
import fr.hc.core.cmd.admin.SpectatorCommand;
import fr.hc.core.cmd.homes.BuyHomeCommand;
import fr.hc.core.cmd.homes.HomeCommand;
import fr.hc.core.cmd.homes.SetHomeCommand;
import fr.hc.core.connection.ConnectionProviderFactory;
import fr.hc.core.connection.HikariConnectionProviderFactory;
import fr.hc.core.db.homes.HomeProvider;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.listeners.CookieSignListener;
import fr.hc.core.listeners.RedstoneLampListener;
import fr.hc.core.tasks.TaskManager;
import fr.hc.core.tasks.async.AsyncTaskExecutor;
import fr.hc.core.tasks.sync.BukkitSyncTaskExecutor;

public class BukkitHeavenCore extends AbstractBukkitPlugin implements HeavenCore
{
	private final ConnectionProviderFactory connectionProviderFactory = new HikariConnectionProviderFactory();
	private HomeProvider homeProvider;
	private UserProvider<? extends UserWithHome> userProvider;
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
		new CookieSignListener(this);
		new RedstoneLampListener(this);

		new HomeCommand(this);
		new SetHomeCommand(this);
		new BuyHomeCommand(this);
		new CreacheatCommand(this);
		new EndercheatCommand(this);
		new HealCommand(this);
		new InventoryCommand(this);
		new RoucoupsCommand(this);
		new SpectatorCommand(this);
	}

	@Override
	public void setReferencePlugin(ReferencePlugin reference)
	{
		homeProvider = new HomeProvider(reference.getConnectionProvider());
		userProvider = reference.getUserProvider();
	}

	@Override
	public ConnectionProviderFactory getConnectionProviderFactory()
	{
		return connectionProviderFactory;
	}

	@Override
	public HomeProvider getHomeProvider()
	{
		return homeProvider;
	}

	@Override
	public UserProvider<? extends UserWithHome> getUserProvider()
	{
		return userProvider;
	}

	@Override
	public TaskManager getTaskManager()
	{
		return taskManager;
	}
}