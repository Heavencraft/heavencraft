package fr.hc.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.StopServerException;

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

	protected void initDatabaseIfNeeded(ConnectionProvider connectionProvider, String testQuery)
			throws StopServerException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(testQuery))
		{
			ps.executeUpdate();
			return;
		}
		catch (final SQLException ex)
		{
		}

		initDatabase(connectionProvider);
	}

	private void initDatabase(ConnectionProvider connectionProvider) throws StopServerException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(getInitDatabaseQuery()))
		{
			ps.executeUpdate();
			return;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new StopServerException();
		}
	}

	private String getInitDatabaseQuery() throws StopServerException
	{
		try
		{
			return Files.readAllLines(new File(getClassLoader().getResource("database.sql").toURI()).toPath()).stream()
					.collect(Collectors.joining("\n"));
		}
		catch (IOException | URISyntaxException e)
		{
			e.printStackTrace();
			throw new StopServerException();
		}
	}
}