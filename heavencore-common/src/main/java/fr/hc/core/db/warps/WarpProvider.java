package fr.hc.core.db.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;

public abstract class WarpProvider<W extends Warp>
{

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String SELECT_WARP_BY_NAME = "SELECT warps.id, warps.name, warps.world, warps.x, warps.y, warps.z, warps.yaw, warps.pitch, "
			+ "users.name AS creator FROM warps JOIN users ON warps.creator = users.id WHERE warps.name = ? LIMIT 1";
	private static final String SELECT_WARPS = "SELECT warps.id, warps.name, warps.world, warps.x, warps.y, warps.z, warps.yaw, warps.pitch, "
			+ "users.name AS creator FROM warps JOIN users ON warps.creator = users.id";
	private static final String INSERT_WARP = "INSERT INTO `warps`(`name`, `creator`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String DELETE_WARP = "DELETE FROM `warps` WHERE `name` = ? LIMIT 1";

	private final ConnectionProvider connectionProvider;
	private WarpFactory<W> warpFactory;

	/**
	 * Constructor
	 * @param connectionProvider
	 * @param factory
	 */
	public WarpProvider(ConnectionProvider connectionProvider, WarpFactory<W> factory)
	{
		this.connectionProvider = connectionProvider;
		this.warpFactory = factory;
	}

	/**
	 * Returns a warp based on his denomination
	 * @param name
	 * @return
	 * @throws HeavenException
	 */
	public Optional<W> getWarpByName(String name) throws HeavenException
	{
		// Get warp from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_WARP_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return Optional.empty();
			// Build a warp using the provided factory
			W wr = warpFactory.newWarp(rs);
			return Optional.of(wr);
		}
		catch (final SQLException e)
		{
			log.error("Error while executing SQL query '{}'", SELECT_WARP_BY_NAME, e);
			throw new DatabaseErrorException();
		}
	}

	/**
	 * Returns a list of warps
	 * @return
	 * @throws HeavenException
	 */
	public List<W> listWarps() throws HeavenException
	{
		// Get warp from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_WARPS))
		{
			List<W> warps = new ArrayList<W>();
			final ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				W wr = warpFactory.newWarp(rs);
				if (wr != null)
					warps.add(wr);
			}

			return warps;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_WARPS, ex);
			throw new DatabaseErrorException();
		}
	}

	/**
	 * Creates a new warp
	 * @param name
	 * @param creator
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param yaw
	 * @param pitch
	 * @throws HeavenException
	 */
	public void createWarp(String name, User creator, String world, double x, double y, double z, float yaw,
			float pitch) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_WARP))
		{
			ps.setString(1, name);
			ps.setInt(2, creator.getId());
			ps.setString(3, world);
			ps.setDouble(4, x);
			ps.setDouble(5, y);
			ps.setDouble(6, z);
			ps.setFloat(7, yaw);
			ps.setFloat(8, pitch);
			ps.executeUpdate();

		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_WARP, ex);
			throw new DatabaseErrorException();
		}
	}

	/**
	 * Deletes a warp
	 * @param name
	 * @throws HeavenException
	 */
	public void deleteWarp(String name) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_WARP))
		{
			ps.setString(1, name);
			ps.executeUpdate();

		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", DELETE_WARP, ex);
			throw new DatabaseErrorException();
		}
	}

}
