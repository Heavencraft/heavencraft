package fr.hc.core.tasks.async;

import java.sql.SQLException;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;

public interface Query extends AsyncTask
{
	@Override
	default void execute() throws HeavenException
	{
		try
		{
			executeQuery();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			throw new DatabaseErrorException();
		}
	}

	void executeQuery() throws HeavenException, SQLException;
}