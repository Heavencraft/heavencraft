package fr.hc.rp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;

public class FirstSpawnListener extends AbstractBukkitListener
{

	public FirstSpawnListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onFirstTimeSpawning(PlayerJoinEvent e)
	{
		final Player player = e.getPlayer();
		final Location spawn = new Location(Bukkit.getWorld("world"), 351, 83, 1041, 90, 0);
		final ItemStack sword, helmet, chestplate, leggings, boots;

		if (!player.hasPlayedBefore())
		{
			player.teleport(spawn);

			sword = new ItemStack(Material.IRON_SWORD);
			helmet = new ItemStack(Material.IRON_HELMET);
			chestplate = new ItemStack(Material.IRON_CHESTPLATE);
			leggings = new ItemStack(Material.IRON_LEGGINGS);
			boots = new ItemStack(Material.IRON_BOOTS);

			final ItemStack[] items = new ItemStack[] { sword, new ItemStack(Material.BREAD, 12) };

			final PlayerInventory inventory = player.getInventory();
			inventory.addItem(items);
			inventory.setHelmet(helmet);
			inventory.setChestplate(chestplate);
			inventory.setLeggings(leggings);
			inventory.setBoots(boots);
		}
	}

}
