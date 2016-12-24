package fr.hc.rp.db.quests.server;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.LocalStringBuilder;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.goals.Goal;
import fr.hc.rp.db.quests.goals.GoalAction;
import fr.hc.rp.db.quests.goals.Goals;

public class ServerQuestInventorySubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();
	private final ServerQuestInventoryListener serverQuestInventoryListener;

	public ServerQuestInventorySubCommand(ServerQuestInventoryListener serverQuestInventoryListener)
	{
		this.serverQuestInventoryListener = serverQuestInventoryListener;
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(sender);
			return;
		}

		final Player player = PlayerUtil.getPlayer(args[0]);
		final ServerQuest quest = plugin.getServerQuestProvider().getById(ConversionUtil.toUint(args[1]));

		final Goals goals = quest.getCurrentStep().getGoals();
		final Goals completed = quest.getCompletedGoals();
		final Goals neededGoals = goals.substract(completed);

		final StringBuilder builder = LocalStringBuilder.get();
		builder.append("Nous aurions besoin de: ");
		for (final Goal goal : neededGoals.getAll())
			if (goal.getAction() == GoalAction.GIVE_ITEM)
				builder.append(goal.getNumber()).append(' ').append(goal.getElement()).append(", ");

		final Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, LocalStringBuilder.release(builder));
		player.openInventory(inventory);
		serverQuestInventoryListener.addContext(new ServerQuestInventoryContext(inventory, quest.getId()));
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{serverquest} inventory <joueur> <quest id>");
	}
}