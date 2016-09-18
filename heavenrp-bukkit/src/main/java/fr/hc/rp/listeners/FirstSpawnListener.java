package fr.hc.rp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.rp.worlds.WorldManager;

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

		PlayerUtil.teleportPlayer(player, WorldManager.getSpawn());

		final PlayerInventory inventory = player.getInventory();
		inventory.addItem(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BREAD, 12));
		inventory.setHelmet(new ItemStack(Material.IRON_HELMET));
		inventory.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		inventory.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		inventory.setBoots(new ItemStack(Material.IRON_BOOTS));
	}
}