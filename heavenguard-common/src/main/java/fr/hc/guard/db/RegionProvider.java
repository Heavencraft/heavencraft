package fr.hc.guard.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.StopServerException;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class RegionProvider
{
	// SQL Queries
	private static final String PRELOAD_REGIONS = "SELECT * FROM regions;";

	private static final String LOAD_REGION = "SELECT * FROM regions WHERE name = LOWER(?) LIMIT 1;";

	private static final String CREATE_REGION = "INSERT INTO regions (name, world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES (LOWER(?), LOWER(?), ?, ?, ?, ?, ?, ?);";
	private static final String DELETE_REGION = "DELETE FROM regions WHERE name = LOWER(?) LIMIT 1;";

	// Logger
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final RegionCache cache = new RegionCache();

	// Connection to the database
	private final ConnectionProvider connectionProvider;

	public RegionProvider(ConnectionProvider connectionProvider) throws StopServerException
	{
		this.connectionProvider = connectionProvider;

		loadFromDatabase();
	}

	/*
	 * Cache
	 */

	private void loadFromDatabase() throws StopServerException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(PRELOAD_REGIONS))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				int count = 0;

				while (rs.next())
				{
					++count;
					cache.addToCache(new Region(connectionProvider, rs, this));
				}

				log.info("{} regions loaded from database.", count);
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new StopServerException(); // Close server if we can't load regions
		}
	}

	private Region loadRegion(String name) throws RegionNotFoundException, DatabaseErrorException
	{
		try (Connection connection = connectionProvider.getConnection(); PreparedStatement ps = connection.prepareStatement(LOAD_REGION))
		{
			ps.setString(1, name);

			try (ResultSet rs = ps.executeQuery())
			{
				if (!rs.next())
					throw new RegionNotFoundException(name);

				final Region region = new Region(connectionProvider, rs, this);
				cache.addToCache(region);
				return region;
			}
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", LOAD_REGION, ex);
			throw new DatabaseErrorException();
		}
	}

	public Region createRegion(String name, String world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) throws HeavenException
	{
		if (cache.regionExists(name))
			throw new HeavenException("La protection {%1$s} existe déjà.", name);

		try (Connection connection = connectionProvider.getConnection(); PreparedStatement ps = connection.prepareStatement(CREATE_REGION))
		{
			ps.setString(1, name);
			ps.setString(2, world);
			ps.setInt(3, minX);
			ps.setInt(4, minY);
			ps.setInt(5, minZ);
			ps.setInt(6, maxX);
			ps.setInt(7, maxY);
			ps.setInt(8, maxZ);

			if (ps.executeUpdate() != 1)
			{
				throw new HeavenException("La region existe déjà");
			}
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", CREATE_REGION, ex);
			throw new DatabaseErrorException();
		}

		return loadRegion(name);
	}

	public void deleteRegion(String name) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_REGION))
		{
			ps.setString(1, name);

			if (ps.executeUpdate() != 1)
				throw new HeavenException("La protection {%1$s} n'a pas pu être supprimée.", name);

			final Optional<Region> optRegion = getRegionByName(name);
			cache.removeFromCache(optRegion.get());
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", DELETE_REGION, ex);
			throw new DatabaseErrorException();
		}
	}

	/*
	 * Retrieving region(s)
	 */

	public Optional<Region> getRegionById(int id)
	{
		final Region region = cache.getRegionById(id);
		return region != null ? Optional.of(region) : Optional.empty();
	}

	public Optional<Region> getRegionByName(String name)
	{
		final Region region = cache.getRegionByName(name);
		return region != null ? Optional.of(region) : Optional.empty();
	}

	public Collection<Region> getRegionsAtLocation(String world, int x, int y, int z)
	{
		final Collection<Region> regionsInWorld = cache.getRegionsInWorld(world);

		// Use HashSet, so we can do equals without caring about the order.
		final Collection<Region> regionsAtLocation = new HashSet<Region>();

		if (regionsInWorld != null)
		{
			for (final Region region : regionsInWorld)
			{
				// Optimization : don't check world twice
				if (region.containsSameWorld(x, y, z))
					regionsAtLocation.add(region);
			}
		}

		return regionsAtLocation;
	}

	public Collection<Region> getRegionsInWorld(String world)
	{
		return cache.getRegionsInWorld(world);
	}

	public boolean regionExists(String name)
	{
		return cache.regionExists(name);
	}
}