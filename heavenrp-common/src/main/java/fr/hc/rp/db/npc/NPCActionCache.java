package fr.hc.rp.db.npc;

import java.util.List;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCActionCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ArrayListValuedHashMap<String, NPCAction> npcActionsByNpcTag = new ArrayListValuedHashMap<String, NPCAction>();

	public List<NPCAction> getByNpcTag(String npcTag)
	{
		return npcActionsByNpcTag.get(npcTag);
	}

	public void addToCache(NPCAction npcMessage)
	{
		npcActionsByNpcTag.put(npcMessage.getNpcTag(), npcMessage);
	}

	public void invalidateCache(NPCAction npcMessage)
	{
		npcActionsByNpcTag.remove(npcMessage.getNpcTag());

		log.info("Invalidated NPC action cache for {}", npcMessage);
	}
}