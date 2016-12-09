package fr.hc.rp.db.npc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;

public class NPCMessageProvider
{
	private static final String SELECT_NPC_MESSAGES_BY_NPC_ID = "SELECT * FROM npc_messages WHERE npc_id = ?;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final NPCMessageCache cache = new NPCMessageCache();
	private final HeavenRP plugin = HeavenRPInstance.get();

	public Collection<NPCMessage> getByNpcId(int npcId) throws DatabaseErrorException
	{
		// Try to get bank account from cache
		Collection<NPCMessage> npcMessages = cache.getByNpcId(npcId);
		if (npcMessages != null)
			return npcMessages;

		// Get user from database
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_NPC_MESSAGES_BY_NPC_ID))
		{
			ps.setInt(1, npcId);

			final ResultSet rs = ps.executeQuery();

			npcMessages = new ArrayList<NPCMessage>();
			while (rs.next())
			{
				final NPCMessage npcMessage = new NPCMessage(rs);
				cache.addToCache(npcMessage);
				npcMessages.add(npcMessage);
			}
			return npcMessages;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_NPC_MESSAGES_BY_NPC_ID, ex);
			throw new DatabaseErrorException();
		}
	}
}