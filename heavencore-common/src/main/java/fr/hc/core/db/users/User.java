package fr.hc.core.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class User
{
	private final int id;
	private final UUID uniqueId;
	private final String name;

	protected User(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		uniqueId = UUID.fromString(rs.getString("uuid"));
		name = rs.getString("name");
	}

	public int getId()
	{
		return id;
	}

	public UUID getUniqueId()
	{
		return uniqueId;
	}

	public String getName()
	{
		return name;
	}
}