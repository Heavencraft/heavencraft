package fr.hc.rp.db.npc;

import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCMessageCache
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final MultiValuedMap<Integer, NPCMessage> npcMessagesByNpcId = new ArrayListValuedHashMap<Integer, NPCMessage>();

	public Collection<NPCMessage> getByNpcId(int npcId)
	{
		return npcMessagesByNpcId.get(npcId);
	}

	public void addToCache(NPCMessage npcMessage)
	{
		npcMessagesByNpcId.put(npcMessage.getNpcId(), npcMessage);
	}

	public void invalidateCache(NPCMessage npcMessage)
	{
		npcMessagesByNpcId.remove(npcMessage.getNpcId());

		log.info("Invalidated NPC message cache for {}", npcMessage);
	}
}