package fr.hc.rp.db.npc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;

public class NPCMessageProvider
{
	private static final String SELECT_NPC_MESSAGES_BY_NPC_TAG = "SELECT * FROM npc_messages WHERE npc_tag = ?;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final NPCMessageCache cache = new NPCMessageCache();
	private final HeavenRP plugin = HeavenRPInstance.get();

	public List<NPCMessage> getByNpcTag(String npcTag) throws DatabaseErrorException
	{
		// Try to get bank account from cache
		List<NPCMessage> npcMessages = cache.getByNpcTag(npcTag);
		if (npcMessages != null)
			return npcMessages;

		// Get user from database
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_NPC_MESSAGES_BY_NPC_TAG))
		{
			ps.setString(1, npcTag);

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
			log.error("Error while executing SQL query '{}'", SELECT_NPC_MESSAGES_BY_NPC_TAG, ex);
			throw new DatabaseErrorException();
		}
	}
}