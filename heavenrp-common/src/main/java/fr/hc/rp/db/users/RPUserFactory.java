package fr.hc.rp.db.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.db.users.UserFactory;

public class RPUserFactory implements UserFactory<RPUser>
{
	@Override
	public RPUser newUser(ResultSet rs) throws SQLException
	{
		return new RPUser(rs);
	}
}