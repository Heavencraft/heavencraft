package fr.hc.rp.db.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;

public class WarpProvider
{

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String SELECT_WARP_BY_NAME = "SELECT warps.id, warps.name, warps.price, warps.world, warps.x, warps.y, warps.z, warps.yaw, warps.pitch, "
			+ "users.name AS creator FROM warps JOIN users ON warps.creator = users.id WHERE warps.name = 'test' LIMIT 1;";
	private static final String SELECT_WARPS = "SELECT warps.id, warps.name, warps.price, warps.world, warps.x, warps.y, warps.z, warps.yaw, warps.pitch, "
			+ "users.name AS creator FROM warps JOIN users ON warps.creator = users.id;";
	private static final String INSERT_WARP = "INSERT INTO `warps`(`name`, `creator`, `price`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String DELETE_WARP = "DELETE FROM `warps` WHERE `name` = ? LIMIT 1;";
	
	
	private final ConnectionProvider connectionProvider;

	public WarpProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public Warp getWarpByName(String name) throws HeavenException
	{
		// Get warp from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_WARP_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return null;
			Warp warp = new Warp(rs);
			return warp;
		}
		catch (final SQLException e)
		{
			log.error("Error while executing SQL query '{}'", SELECT_WARP_BY_NAME, e);
			throw new DatabaseErrorException();
		}
	}

	public List<Warp> listWarps() throws HeavenException
	{
		// Get warp from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_WARPS))
		{
			List<Warp> warps = new ArrayList<Warp>();
			final ResultSet rs = ps.executeQuery();
			while (rs.next())
				warps.add(new Warp(rs));
			
			return warps;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_WARP, ex);
			throw new DatabaseErrorException();
		}
	}

	public void createWarp(String name, User creator, String world, int price, double x, double y, double z, float yaw,
			float pitch) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_WARP))
		{
			ps.setString(1, name);
			ps.setInt(2, creator.getId());
			ps.setInt(3, price);
			ps.setString(2, world);
			ps.setDouble(3, x);
			ps.setDouble(4, y);
			ps.setDouble(5, z);
			ps.setFloat(6, yaw);
			ps.setFloat(7, pitch);
			ps.executeQuery();

		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_WARP, ex);
			throw new DatabaseErrorException();
		}
	}
	
	public void deleteWarp(String name) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_WARP))
		{
			ps.setString(1, name);
			ps.executeQuery();
			
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", DELETE_WARP, ex);
			throw new DatabaseErrorException();
		}
	}

}
