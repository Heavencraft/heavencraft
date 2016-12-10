package fr.hc.rp.db.npc;

import java.util.List;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCMessageCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final ArrayListValuedHashMap<String, NPCMessage> npcMessagesByNpcTag = new ArrayListValuedHashMap<String, NPCMessage>();

	public List<NPCMessage> getByNpcTag(String npcTag)
	{
		return npcMessagesByNpcTag.get(npcTag);
	}

	public void addToCache(NPCMessage npcMessage)
	{
		npcMessagesByNpcTag.put(npcMessage.getNpcTag(), npcMessage);
	}

	public void invalidateCache(NPCMessage npcMessage)
	{
		npcMessagesByNpcTag.remove(npcMessage.getNpcTag());

		log.info("Invalidated NPC message cache for {}", npcMessage);
	}
}