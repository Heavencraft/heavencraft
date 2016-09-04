package fr.hc.guard.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.connection.ConnectionProvider;

public class GlobalRegion
{
	private final String name;

	private final FlagHandler flagHandler;

	public GlobalRegion(ConnectionProvider connectionHandler, ResultSet rs) throws SQLException
	{
		name = rs.getString("name");

		// Load flags
		flagHandler = new FlagHandler(connectionHandler, rs, null);
	}

	public String getName()
	{
		return name;
	}

	public FlagHandler getFlagHandler()
	{
		return flagHandler;
	}

	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		str.append(name);
		str.append(" [");
		str.append(flagHandler);
		str.append("]");
		return str.toString();
	}
}