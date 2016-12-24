package fr.hc.rp.db.quests.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.db.quests.goals.Goal;
import fr.hc.rp.db.quests.goals.GoalAction;
import fr.hc.rp.db.quests.goals.Goals;
import fr.hc.rp.worlds.WorldsManager;

public class ServerQuestInventoryListener extends AbstractBukkitListener
{
	private final Map<Inventory, ServerQuestInventoryContext> contextsByInventory = new HashMap<Inventory, ServerQuestInventoryContext>();
	private final HeavenRP plugin;

	public ServerQuestInventoryListener(BukkitHeavenRP plugin)
	{
		super(plugin);
		this.plugin = plugin;
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
		final ServerQuestStep currentStep = quest.getCurrentStep();

		final Goals requiredGoals = currentStep.getGoals();
		final Goals completedGoals = quest.getCompletedGoals();
		final Goals newCompletedGoals = new Goals();

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

		// All the goals are completed, the step is over, moving to next step
		if (newCompletedGoals.equals(requiredGoals))
		{
			final Optional<ServerQuestStep> optNextStep = currentStep.getNextStep();

			if (!optNextStep.isPresent())
			{
				log.error("Non final step doesn't have a next step.");
				ChatUtil.sendMessage(player, UnexpectedErrorException.MESSAGE);
				return;
			}

			new UpdateServerQuestCurrentStepQuery(quest, optNextStep.get(), plugin.getServerQuestProvider())
			{
				@Override
				public void onException(HeavenException ex)
				{
					ChatUtil.sendMessage(player, ex.getMessage());
				}

				@Override
				public void onSuccess()
				{
					try
					{
						WorldEditUtil.pasteAtOrigin(currentStep.getSchematic(), WorldsManager.getWorld());
					}
					catch (final HeavenException ex)
					{
						log.error("Unable to paste schematic", ex);
					}
				}
			}.schedule();
		}

		// There are still uncompleted goals, we just update the quest status
		else
		{
			new UpdateServerQuestCompletedGoalsQuery(quest, newCompletedGoals, plugin.getServerQuestProvider())
			{
				@Override
				public void onException(HeavenException ex)
				{
					ChatUtil.sendMessage(player, ex.getMessage());
				}
			}.schedule();
		}
	}

	private void giveBack(Player player, ItemStack item)
	{
		player.getInventory().addItem(item);
		ChatUtil.sendMessage(player, "Je n'ai pas besoin de ces %1$s %2$s, je te les rends donc.", item.getAmount(), item.getType());
	}
}