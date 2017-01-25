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

public class NPCActionProvider
{
	private static final String SELECT_NPC_ACTIONS_BY_NPC_TAG = "SELECT * FROM npc_actions WHERE npc_tag = ?;";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final NPCActionCache cache = new NPCActionCache();
	private final HeavenRP plugin = HeavenRPInstance.get();

	public List<NPCAction> getByNpcTag(String npcTag) throws DatabaseErrorException
	{
		// Try to get bank account from cache
		List<NPCAction> npcActions = cache.getByNpcTag(npcTag);
		if (!npcActions.isEmpty())
			return npcActions;

		// Get user from database
		try (Connection connection = plugin.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_NPC_ACTIONS_BY_NPC_TAG))
		{
			ps.setString(1, npcTag);

			final ResultSet rs = ps.executeQuery();

			npcActions = new ArrayList<NPCAction>();
			while (rs.next())
			{
				final NPCAction npcAction = new NPCAction(rs);
				cache.addToCache(npcAction);
				npcActions.add(npcAction);
			}
			return npcActions;
		}
		catch (final SQLException ex)
		{
			log.error("Error while executing SQL query '{}'", SELECT_NPC_ACTIONS_BY_NPC_TAG, ex);
			throw new DatabaseErrorException();
		}
	}
}