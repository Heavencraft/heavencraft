package fr.hc.rp.db.quests.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.db.quests.goals.Goal;
import fr.hc.rp.db.quests.goals.GoalAction;
import fr.hc.rp.db.quests.goals.Goals;

public class ServerQuestInventoryListener extends AbstractBukkitListener
{
	private final Map<Inventory, ServerQuestInventoryContext> contextsByInventory = new HashMap<Inventory, ServerQuestInventoryContext>();

	public ServerQuestInventoryListener(BukkitHeavenRP plugin)
	{
		super(plugin);
	}

	public void addContext(ServerQuestInventoryContext context)
	{
		contextsByInventory.put(context.getInventory(), context);
	}

	@EventHandler
	private void onInventoryClose(InventoryCloseEvent event) throws HeavenException
	{
		final Inventory inventory = event.getInventory();
		final Player player = (Player) event.getPlayer();

		final ServerQuestInventoryContext context = contextsByInventory.remove(inventory);
		if (context == null)
			return;

		final ServerQuest quest = context.getQuest();
		final Goals requiredGoals = quest.getCurrentStep().getGoals();
		final Goals completedGoals = quest.getCompletedGoals();
		final Collection<Goal> newCompletedGoals = new ArrayList<Goal>();

		for (final ItemStack item : inventory.getContents())
		{
			// Ignore empty ItemStack
			if (item == null)
				continue;

			final Goal requiredGoal = requiredGoals.get(GoalAction.GIVE_ITEM, item.getType().name());
			if (requiredGoal == null)
			{
				giveBack(player, item);
				continue;
			}

			final Goal completedGoal = completedGoals.get(GoalAction.GIVE_ITEM, item.getType().name());
			final int neededNumber = requiredGoal.getNumber() - (completedGoal != null ? completedGoal.getNumber() : 0);

			if (neededNumber <= 0)
			{
				giveBack(player, item);
				newCompletedGoals.add(completedGoal); // No change
			}
			else if (neededNumber >= item.getAmount())
			{
				ChatUtil.sendMessage(player, "Je prend ces %1$s %2$s.", item.getAmount(), item.getType());
				newCompletedGoals.add(completedGoal.add(item.getAmount()));
			}
			else if (neededNumber < item.getAmount())
			{
				ChatUtil.sendMessage(player, "Je prend ces %1$s %2$s.", neededNumber, item.getType());
				final ItemStack item2 = new ItemStack(item);
				item2.setAmount(item.getAmount() - neededNumber);
				giveBack(player, item2);
				newCompletedGoals.add(completedGoal.add(neededNumber));
			}
		}
	}

	private void giveBack(Player player, ItemStack item)
	{
		player.getInventory().addItem(item);
		ChatUtil.sendMessage(player, "Je n'ai pas besoin de ces %1$s %2$s, je te les rends donc.", item.getAmount(),
				item.getType());
	}
}