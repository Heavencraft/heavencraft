package fr.hc.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.connection.ConnectionProvider;

public abstract class AbstractBukkitPlugin extends JavaPlugin
{
	protected ConnectionProvider createConnectionProvider(FileConfiguration config)
	{
		final String username = config.getString("mysql.username");
		final String password = config.getString("mysql.password");
		final String database = config.getString("mysql.database");

		return HeavenCoreInstance.get().getConnectionProviderFactory().newConnectionProvider(database, username,
				password);
	}
}