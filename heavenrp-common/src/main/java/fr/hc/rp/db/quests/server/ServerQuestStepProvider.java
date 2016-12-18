package fr.hc.rp.db.quests.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.exceptions.ServerQuestNotFoundException;

public class ServerQuestStepProvider
{
	private static final String SELECT_SERVER_QUEST_STEP_BY_ID = "SELECT * FROM server_quest_steps WHERE id = ? LIMIT 1;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ServerQuestStepCache cache = new ServerQuestStepCache();
	private final HeavenRP plugin = HeavenRPInstance.get();

	public Optional<ServerQuestStep> getOptionalById(int id) throws DatabaseErrorException
	{
		// Try to get ServerQuestStep from cache
		ServerQuestStep serverQuestStep = cache.getById(id);
		if (serverQuestStep != null)
			return Optional.of(serverQuestStep);

		// Get ServerQuestStep from database
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_SERVER_QUEST_STEP_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return Optional.empty();

			serverQuestStep = new ServerQuestStep(rs);
			cache.addToCache(serverQuestStep);
			return Optional.of(serverQuestStep);
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_SERVER_QUEST_STEP_BY_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public ServerQuestStep getById(int id) throws HeavenException
	{
		final Optional<ServerQuestStep> optCServerQuestStep = getOptionalById(id);
		if (!optCServerQuestStep.isPresent())
			throw new ServerQuestNotFoundException(id);
		return optCServerQuestStep.get();
	}
}