package fr.hc.rp.db.quests.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerQuestCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, ServerQuest> serverQuestsById = new ConcurrentHashMap<Integer, ServerQuest>();

	public ServerQuest getById(int id)
	{
		return serverQuestsById.get(id);
	}

	public void addToCache(ServerQuest serverQuest)
	{
		serverQuestsById.put(serverQuest.getId(), serverQuest);
	}

	public void invalidateCache(ServerQuest serverQuest)
	{
		serverQuestsById.remove(serverQuest.getId());

		log.info("Invalidated server quest cache for {}", serverQuest);
	}
}