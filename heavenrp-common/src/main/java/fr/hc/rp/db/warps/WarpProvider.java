package fr.hc.rp.db.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;

public abstract class WarpProvider
{

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String SELECT_WARP_BY_NAME = "SELECT * FROM warps WHERE name = ? LIMIT 1;";
	private static final String INSERT_WARP = "INSERT INTO warps (name, world, x, y, z, yaw, pitch, price, magicpoints) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private final ConnectionProvider connectionProvider;

	public WarpProvider(ConnectionProvider connectionProvider)
	{
		super();
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
			if(!rs.next())
				return null;
			Warp warp = new Warp(rs);
			return warp;
		}
		catch (SQLException e)
		{
			log.error("Error while executing SQL query '{}'", SELECT_WARP_BY_NAME, e);
			throw new DatabaseErrorException();
		}
	}

	public void createWarp(String name, String world, double x, double y, double z, float yaw, float pitch, int price,
			int magicpoints) throws HeavenException
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_WARP))
		{
			ps.setString(1, name);
			ps.setString(2, world);
			ps.setDouble(3, x);
			ps.setDouble(4, y);
			ps.setDouble(5, z);
			ps.setFloat(6, yaw);
			ps.setFloat(7, pitch);
			ps.setInt(8, price);
			ps.setInt(9, magicpoints);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", INSERT_WARP, ex);
			throw new DatabaseErrorException();
		}
	}

}
