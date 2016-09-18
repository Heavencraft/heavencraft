package fr.hc.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.StopServerException;

public abstract class AbstractDatabaseBukkitPlugin extends AbstractBukkitPlugin
{
	protected final ConnectionProvider connectionProvider;
	private final String testQuery;

	protected AbstractDatabaseBukkitPlugin(String testQuery)
	{
		this.testQuery = testQuery;
		connectionProvider = createConnectionProvider(getConfig());

		log.info("Initilized, connectionProvider = {}", connectionProvider);
	}

	private ConnectionProvider createConnectionProvider(FileConfiguration config)
	{
		final String username = config.getString("mysql.username");
		final String password = config.getString("mysql.password");
		final String database = config.getString("mysql.database");

		log.info("Creating connectionProvider with user={}, database={}", username, database);
		return HeavenCoreInstance.get().getConnectionProviderFactory().newConnectionProvider(database, username,
				password);
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	protected List<String> getInitializationQueriesIfNeeded() throws StopServerException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(testQuery))
		{
			ps.executeQuery();
			return Collections.emptyList();
		}
		catch (final SQLException ex)
		{
			return getInitializationQueries();
		}
	}

	private List<String> getInitializationQueries() throws StopServerException
	{
		String queries = "";

		try (InputStream in = getClassLoader().getResourceAsStream("database.sql");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				if (line.trim().isEmpty() || line.trim().startsWith("--"))
					continue;

				queries += line;
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			throw new StopServerException();
		}

		return Arrays.asList(queries.split(";"));
	}
}