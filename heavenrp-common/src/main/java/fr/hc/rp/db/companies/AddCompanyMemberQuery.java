package fr.hc.rp.db.companies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.Query;

public class AddCompanyMemberQuery implements Query
{
	private static final String QUERY = "INSERT INTO companies_users (company_id, user_id, employer) VALUES (?, ?, ?);";

	private final Company company;
	private final User user;
	private final boolean isEmployer;
	private final CompanyProvider provider;

	public AddCompanyMemberQuery(Company company, User user, boolean isEmployer, CompanyProvider provider)
	{
		this.company = company;
		this.user = user;
		this.isEmployer = isEmployer;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (isEmployer)
		{
			if (company.isEmployer(user))
				return; // Nothing to do
		}
		else
		{
			if (company.isEmployee(user))
				return; // Nothing to do
		}

		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, company.getId());
			ps.setInt(2, user.getId());
			ps.setBoolean(3, isEmployer);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() != 1)
				throw new DatabaseErrorException();

			provider.invalidateCache(company);
		}
	}
}