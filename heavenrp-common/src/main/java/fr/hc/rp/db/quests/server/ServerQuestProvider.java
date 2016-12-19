package fr.hc.rp.db.quests.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.exceptions.ServerQuestNotFoundException;

public class ServerQuestProvider
{
	private static final String SELECT_SERVER_QUEST_BY_ID = "SELECT * FROM server_quests WHERE id = ? LIMIT 1;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ServerQuestCache cache = new ServerQuestCache();
	private final HeavenRP plugin = HeavenRPInstance.get();

	public ServerQuest getById(int id) throws HeavenException
	{
		// Try to get server quest from cache
		ServerQuest serverQuest = cache.getById(id);
		if (serverQuest != null)
			return serverQuest;

		// Get user from database
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_SERVER_QUEST_BY_ID))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new ServerQuestNotFoundException(id);

			serverQuest = new ServerQuest(rs);
			cache.addToCache(serverQuest);
			return serverQuest;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_SERVER_QUEST_BY_ID, ex);
			throw new DatabaseErrorException();
		}
	}

	public ConnectionProvider getConnectionProvider()
	{
		return plugin.getConnectionProvider();
	}

	public void invalidateCache(ServerQuest quest)
	{
		cache.invalidateCache(quest);
	}
}