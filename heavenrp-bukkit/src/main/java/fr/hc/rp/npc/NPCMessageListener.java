package fr.hc.rp.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.npc.ConditionExecutor;
import fr.hc.rp.db.npc.NPCAction;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

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
	private void onNPCRightClick(NPCRightClickEvent event) throws HeavenException
	{
		log.info("onNPCRightClick");

		final NPC npc = event.getNPC();
		final String npcTag = npc.data().get(NPC_TAG);
		if (npcTag == null)
			return;

		log.info("onNPCRightClick with npcTag = {}", npcTag);

		List<NPCAction> actions = HeavenRPInstance.get().getNpcMessageProvider().getByNpcTag(npcTag);
		actions = filterUsingConditions(actions);
		if (actions.isEmpty())
			return;

		log.info("onNPCRightClick with npcTag = {} and {} associated actions", npcTag, actions.size());
		final NPCAction action = selectAction(actions);

		final Player player = event.getClicker();
		final String npcName = npc.getName();

		if (action.hasMessages())
			for (final String message : action.getMessages())
				ChatUtil.sendMessage(player, NPC_MESSAGE_FORMAT, npcName, replaceVariables(message, player));

		if (action.hasCommands())
			for (final String command : action.getCommands())
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replaceVariables(command, player));
	}

	private List<NPCAction> filterUsingConditions(List<NPCAction> actions) throws HeavenException
	{
		final List<NPCAction> defaultActions = new ArrayList<NPCAction>();
		final List<NPCAction> conditionedActions = new ArrayList<NPCAction>();

		for (final NPCAction action : actions)
		{
			if (!action.hasConditions())
			{
				defaultActions.add(action);
				continue;
			}

			log.info("evaluating {}", action.getConditions());
			if (ConditionExecutor.evaluate(action.getConditions()))
				conditionedActions.add(action);
		}

		return !conditionedActions.isEmpty() ? conditionedActions : defaultActions;
	}

	private NPCAction selectAction(List<NPCAction> actions)
	{
		if (actions.size() == 1)
			return actions.get(0);
		else
			return actions.get(random.nextInt(actions.size()));
	}

	// Replace variables by their current values
	// TODO: optimize
	private String replaceVariables(String command, Player player)
	{
		command = command.replaceAll("%playerName%", player.getName());

		return command;
	}
}