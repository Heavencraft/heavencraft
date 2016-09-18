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
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		if (player.hasPlayedBefore())
			return;

		final Location spawn = new Location(Bukkit.getWorld("world"), 351, 83, 1041, 90, 0);

		player.teleport(spawn);

		final PlayerInventory inventory = player.getInventory();
		inventory.addItem(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BREAD, 12));
		inventory.setHelmet(new ItemStack(Material.IRON_HELMET));
		inventory.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		inventory.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		inventory.setBoots(new ItemStack(Material.IRON_BOOTS));
	}
}