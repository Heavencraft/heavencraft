package fr.hc.rp.npc;

import java.util.List;
import java.util.Random;

import org.bukkit.event.EventHandler;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.npc.NPCMessage;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCMessageListener extends AbstractBukkitListener
{
	public static final String NPC_TAG = "NPC_TAG";
	private static final String NPC_MESSAGE_FORMAT = "(%1$s) %2$s";

	private final Random random = new Random();

	public NPCMessageListener(BukkitHeavenRP plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onNPCRightClick(NPCRightClickEvent event) throws DatabaseErrorException
	{
		log.info("onNPCRightClick");

		final String npcTag = event.getNPC().data().get(NPC_TAG);
		if (npcTag == null)
			return;

		log.info("onNPCRightClick with npcTag = {}", npcTag);

		// TODO: conditional messsages
		final List<NPCMessage> messages = HeavenRPInstance.get().getNpcMessageProvider().getByNpcTag(npcTag);
		if (messages.isEmpty())
			return;

		log.info("onNPCRightClick with npcTag = {} and {} associated messages", npcTag, messages.size());
		final NPCMessage message = messages.get(random.nextInt(messages.size()));
		ChatUtil.sendMessage(event.getClicker(), NPC_MESSAGE_FORMAT, event.getNPC().getName(), message.getMessage());
	}
}