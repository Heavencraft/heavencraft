package fr.hc.rp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;

public class FirstSpawnListener extends AbstractBukkitListener
{

	public FirstSpawnListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	public void onFirstTimeSpawning(PlayerJoinEvent e)
	{
		final Player player = e.getPlayer();
		final Location spawn = new Location(Bukkit.getWorld("world"), 351, 83, 1041, 90, 0);

		if (!player.hasPlayedBefore())
			player.teleport(spawn);
	}

}
