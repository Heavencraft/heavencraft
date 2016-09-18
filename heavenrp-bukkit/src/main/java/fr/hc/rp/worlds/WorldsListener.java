package fr.hc.rp.worlds;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.utils.chat.ChatUtil;

public class WorldsListener extends AbstractBukkitListener
{
	public WorldsListener(AbstractBukkitPlugin plugin)
	{
		super(plugin);
	}

	// Limites des mondes
	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Location l = event.getTo();

		// Monde semi-RP
		if (l.getWorld().equals(WorldsManager.getWorld()))
		{
			if (Math.pow(l.getX(), 2) + Math.pow(l.getZ(), 2) > 6250000)
			{
				event.setTo(WorldsManager.getSpawn());
				ChatUtil.sendMessage(event.getPlayer(), "Vous avez atteint la limite du monde semi-RP.");
				// event.setCancelled(true);
			}
		}

		// Monde ressources
		else if (l.getWorld().equals(WorldsManager.getResources()))
		{
			final int limit = WorldsManager.RESOURCES_SIZE / 2;
			final int limit_with_warn_offset = limit - 20;

			if (Math.abs(l.getX()) > limit || Math.abs(l.getZ()) > limit)
			{
				event.setTo(WorldsManager.getSpawn());
				ChatUtil.sendMessage(event.getPlayer(), "Vous avez atteint la limite du monde ressources.");
				// event.setCancelled(true);
			}
			else if (Math.abs(l.getX()) > (limit_with_warn_offset) || Math.abs(l.getZ()) > (limit_with_warn_offset))
			{
				ChatUtil.sendMessage(event.getPlayer(), "Vous vous approchez de la limite du monde ressources.");
			}
		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		final Chunk spawnChunk = WorldsManager.getSpawn().getChunk();
		// If is spawn chunk, do not unload
		if (event.getChunk().getWorld().getName().equals(spawnChunk.getWorld().getName())
				&& event.getChunk().getX() == spawnChunk.getX() && event.getChunk().getZ() == spawnChunk.getZ())
		{
			event.setCancelled(true);
			return;
		}
	}
}