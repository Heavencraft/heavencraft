package fr.hc.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
			ps.executeQuery();
			return;
		}
		catch (final SQLException ex)
		{
			initDatabase(connectionProvider);
		}
	}

	private void initDatabase(ConnectionProvider connectionProvider) throws StopServerException
	{
		try (Connection connection = connectionProvider.getConnection();
				Statement statement = connection.createStatement())
		{
			for (final String query : getInitDatabaseQueries())
				if (!query.trim().isEmpty())
					statement.execute(query);

			return;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new StopServerException();
		}
	}

	private String[] getInitDatabaseQueries() throws StopServerException
	{
		String queries = "";

		try (InputStream in = getClassLoader().getResourceAsStream("database.sql");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();

				if (line.isEmpty() || line.startsWith("--"))
					continue;

				queries += line;
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			throw new StopServerException();
		}

		return queries.split(";");
	}
}