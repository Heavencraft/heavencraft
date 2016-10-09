package fr.hc.core.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public abstract class AbstractUser implements User
{
	private final int id;
	private final UUID uniqueId;
	private final String name;
	private final Timestamp lastLogin;

	protected AbstractUser(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		uniqueId = UUID.fromString(rs.getString("uuid"));
		name = rs.getString("name");
		lastLogin = rs.getTimestamp("last_login");
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public UUID getUniqueId()
	{
		return uniqueId;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Timestamp getLastLogin()
	{
		return lastLogin;
	}
}