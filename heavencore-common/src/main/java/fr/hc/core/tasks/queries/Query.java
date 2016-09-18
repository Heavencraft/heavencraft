package fr.hc.core.tasks.queries;

import java.sql.SQLException;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.async.AsyncTask;

public interface Query extends AsyncTask
{
	@Override
	default void execute() throws HeavenException
	{
		try
		{
			executeQuery();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new DatabaseErrorException();
		}
	}

	void executeQuery() throws HeavenException, SQLException;
}