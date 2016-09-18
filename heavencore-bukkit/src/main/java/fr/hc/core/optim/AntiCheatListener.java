package fr.hc.core.optim;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.bookbuilder.BookBuilder;

public class AntiCheatListener extends AbstractBukkitListener
{
	public AntiCheatListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	// OPTI : doit s'exécuter avant l'anti-lag.
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event)
	{
		switch (event.getEntityType())
		{
			case IRON_GOLEM:
				if (event.getSpawnReason() == SpawnReason.VILLAGE_DEFENSE)
					event.setCancelled(true);
				break;

			case VILLAGER:
				if (event.getSpawnReason() == SpawnReason.BREEDING)
					event.setCancelled(true);
				break;

			default:
				break;
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event)
	{
		if (event.getBlock().getType() == Material.MOB_SPAWNER)
			event.setCancelled(true);
	}

	// Avoid trading of books made from the plugin
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getInventory().getType() != InventoryType.MERCHANT)
			return;

		final ItemStack item = event.getCurrentItem();
		if (item == null || item.getType() != Material.WRITTEN_BOOK)
			return;

		final BookMeta book = (BookMeta) item.getItemMeta();
		if (book.getAuthor().equals(BookBuilder.HEAVENCRAFT))
		{
			event.setCancelled(true);
		}
	}
}