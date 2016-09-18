package fr.hc.rp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;

public class RespawnListener extends AbstractBukkitListener
{
	public RespawnListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		final Player player = e.getPlayer();
		final Location spawn = new Location(Bukkit.getWorld("world"), 351, 83, 1041, 90, 0);

		player.teleport(spawn);
	}
}
