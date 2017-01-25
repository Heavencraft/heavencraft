package fr.hc.rp.db.quests.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerQuestStepCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, ServerQuestStep> serverQuestStepsById = new ConcurrentHashMap<Integer, ServerQuestStep>();

	public ServerQuestStep getById(int id)
	{
		return serverQuestStepsById.get(id);
	}

	public void addToCache(ServerQuestStep serverQuestStep)
	{
		serverQuestStepsById.put(serverQuestStep.getId(), serverQuestStep);
	}

	public void invalidateCache(ServerQuestStep serverQuestStep)
	{
		serverQuestStepsById.remove(serverQuestStep.getId());

		log.info("Invalidated server quest step cache for {}", serverQuestStep);
	}
}