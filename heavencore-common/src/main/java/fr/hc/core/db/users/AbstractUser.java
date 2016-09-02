package fr.hc.core.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class AbstractUser implements User
{
	private final int id;
	private final UUID uniqueId;
	private final String name;

	protected AbstractUser(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		uniqueId = UUID.fromString(rs.getString("uuid"));
		name = rs.getString("name");
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
}