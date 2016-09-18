package fr.hc.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import fr.hc.core.cmd.admin.CreacheatCommand;
import fr.hc.core.cmd.admin.EndercheatCommand;
import fr.hc.core.cmd.admin.HealCommand;
import fr.hc.core.cmd.admin.InventoryCommand;
import fr.hc.core.cmd.admin.RoucoupsCommand;
import fr.hc.core.cmd.admin.SpectatorCommand;
import fr.hc.core.cmd.homes.BuyHomeCommand;
import fr.hc.core.cmd.homes.HomeCommand;
import fr.hc.core.cmd.homes.SetHomeCommand;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.connection.ConnectionProviderFactory;
import fr.hc.core.connection.HikariConnectionProviderFactory;
import fr.hc.core.db.homes.HomeProvider;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.db.users.home.UserWithHome;
import fr.hc.core.exceptions.StopServerException;
import fr.hc.core.listeners.CookieSignListener;
import fr.hc.core.listeners.NoChatListener;
import fr.hc.core.listeners.RedstoneLampListener;
import fr.hc.core.tasks.TaskManager;
import fr.hc.core.tasks.async.AsyncTaskExecutor;
import fr.hc.core.tasks.sync.BukkitSyncTaskExecutor;

public class BukkitHeavenCore extends AbstractBukkitPlugin implements HeavenCore
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ConnectionProviderFactory connectionProviderFactory = new HikariConnectionProviderFactory();
	private HomeProvider homeProvider;
	private UserProvider<? extends UserWithHome> userProvider;
	private TaskManager taskManager;

	public BukkitHeavenCore()
	{
		HeavenCoreInstance.set(this);
	}

	private static final String CREATE_TABLE_QUERY = "CREATE TABLE";
	private static final String ALTER_TABLE_QUERY = "ALTER TABLE";
	private static final String INSERT_INTO_QUERY = "INSERT INTO";

	private void initializeDatabase() throws StopServerException
	{
		final Multimap<ConnectionProvider, String> createTableQueries = HashMultimap.create();
		final Multimap<ConnectionProvider, String> alterTableQueries = HashMultimap.create();
		final Multimap<ConnectionProvider, String> insertIntoQueries = HashMultimap.create();

		// Get the queries from Heavencraft's plugins
		for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			if (!(plugin instanceof AbstractDatabaseBukkitPlugin))
				continue;

			final AbstractDatabaseBukkitPlugin heavenPlugin = (AbstractDatabaseBukkitPlugin) plugin;

			final ConnectionProvider connectionProvider = heavenPlugin.getConnectionProvider();

			for (String query : heavenPlugin.getInitializationQueriesIfNeeded())
			{
				query = query.trim();

				if (query.startsWith(CREATE_TABLE_QUERY))
					createTableQueries.put(connectionProvider, query);
				else if (query.startsWith(ALTER_TABLE_QUERY))
					alterTableQueries.put(connectionProvider, query);
				else if (query.startsWith(INSERT_INTO_QUERY))
					insertIntoQueries.put(connectionProvider, query);
				else
				{
					log.error("Invalid initialization query: {}", query);
					throw new StopServerException();
				}
			}
		}

		try
		{
			for (final Entry<ConnectionProvider, Collection<String>> entry : createTableQueries.asMap().entrySet())
			{
				try (final Connection connection = entry.getKey().getConnection();
						final Statement statement = connection.createStatement())
				{
					for (final String query : entry.getValue())
						statement.execute(query);
				}
			}

			for (final Entry<ConnectionProvider, Collection<String>> entry : alterTableQueries.asMap().entrySet())
			{
				try (final Connection connection = entry.getKey().getConnection();
						final Statement statement = connection.createStatement())
				{
					for (final String query : entry.getValue())
						statement.execute(query);
				}
			}

			for (final Entry<ConnectionProvider, Collection<String>> entry : insertIntoQueries.asMap().entrySet())
			{
				try (final Connection connection = entry.getKey().getConnection();
						final Statement statement = connection.createStatement())
				{
					for (final String query : entry.getValue())
						statement.execute(query);
				}
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new StopServerException();
		}
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		try
		{
			initializeDatabase();

			taskManager = new TaskManager(new BukkitSyncTaskExecutor(), new AsyncTaskExecutor());
			new CookieSignListener(this);
			new RedstoneLampListener(this);
			new NoChatListener(this);

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
		catch (final StopServerException e)
		{
			e.printStackTrace();

			Bukkit.shutdown();
		}
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