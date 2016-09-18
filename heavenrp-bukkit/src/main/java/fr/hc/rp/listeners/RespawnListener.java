package fr.hc.rp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.rp.worlds.WorldManager;

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

		player.teleport(WorldManager.getSpawn());
	}
}
