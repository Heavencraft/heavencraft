package fr.hc.core.db.users.balance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class UpdateUserBalanceQuery implements Query
{
	private static final String QUERY = "UPDATE users SET balance = balance + ? WHERE id = ? AND balance + ? >= 0 LIMIT 1;";

	private final UserWithBalance user;
	private final int delta;
	private final UserProvider<? extends UserWithBalance> userProvider;

	public UpdateUserBalanceQuery(UserWithBalance user, int delta, UserProvider<? extends UserWithBalance> userProvider)
	{
		this.user = user;
		this.delta = delta;
		this.userProvider = userProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (delta == 0)
			return; // Nothing to do

		try (Connection connection = userProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, delta);
			ps.setInt(2, user.getId());
			ps.setInt(3, delta);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new HeavenException("Vous fouillez dans votre bourse... Vous n'avez pas assez.");

			userProvider.invalidateCache(user);
		}
	}
}