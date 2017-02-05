package fr.hc.guard.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.StopServerException;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class GlobalRegionProvider
{
	private static final String PRELOAD_GLOBAL_REGIONS = "SELECT * FROM worlds;";
	private static final String LOAD_GLOBAL_REGION = "SELECT * FROM worlds WHERE name = LOWER(?) LIMIT 1;";
	private static final String CREATE_GLOBAL_REGION = "INSERT INTO worlds (name) VALUES (LOWER(?));";

	// Logger
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final GlobalRegionCache cache = new GlobalRegionCache();

	// Connection to the database
	private final ConnectionProvider connectionProvider;

	public GlobalRegionProvider(ConnectionProvider connectionProvider) throws StopServerException
	{
		this.connectionProvider = connectionProvider;

		preloadGlobalRegions();
	}

	private void preloadGlobalRegions() throws StopServerException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(PRELOAD_GLOBAL_REGIONS))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				int count = 0;

				while (rs.next())
				{
					++count;
					cache.addToCache(new GlobalRegion(connectionProvider, rs));
				}

				log.info("{} global regions loaded from database.", count);
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new StopServerException(); // Close server if we can't load regions
		}
	}

	private GlobalRegion loadGlobalRegion(String world) throws RegionNotFoundException, DatabaseErrorException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(LOAD_GLOBAL_REGION))
		{
			ps.setString(1, world);

			try (ResultSet rs = ps.executeQuery())
			{
				if (!rs.next())
					throw new RegionNotFoundException(world);

				final GlobalRegion region = new GlobalRegion(connectionProvider, rs);
				cache.addToCache(region);
				return region;
			}
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", LOAD_GLOBAL_REGION, ex);
			throw new DatabaseErrorException();
		}
	}

	public GlobalRegion createGlobalRegion(String world) throws HeavenException
	{
		if (cache.globalRegionExists(world))
			throw new HeavenException("La protection {%1$s} existe déjà.", world);

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(CREATE_GLOBAL_REGION))
		{
			ps.setString(1, world);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("La region existe déjà");
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", CREATE_GLOBAL_REGION, ex);
			throw new DatabaseErrorException();
		}

		return loadGlobalRegion(world);
	}

	public GlobalRegion getGlobalRegion(String world)
	{
		return cache.getGlobalRegionByWorld(world);
	}
}