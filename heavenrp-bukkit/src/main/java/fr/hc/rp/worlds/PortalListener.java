package fr.hc.rp.worlds;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.utils.PlayerUtil;

public class PortalListener extends AbstractBukkitListener
{
	public PortalListener(JavaPlugin plugin)
	{
		super(plugin);
	}

	// Passage dans un portail
	@EventHandler
	public void onEntityPortalEnter(PlayerMoveEvent event)
	{
		final Location from = event.getFrom();
		final Location to = event.getTo();

		if (to.getWorld() != WorldManager.getWorld())
			return;

		// Player enter into a portal
		final Block portalBlock = to.getBlock();
		if (from.getBlock().getType() != Material.PORTAL && portalBlock.getType() == Material.PORTAL)
		{
			final Location destination;
			switch (portalBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType())
			{
				case RED_NETHER_BRICK:
					destination = WorldManager.getSpawnNether();
					break;
				case ENDER_STONE:
					destination = WorldManager.getSpawnTheEnd();
					break;
				case LOG:
					destination = WorldManager.getResourcesSpawn();
					break;
				default:
					return;
			}
			PlayerUtil.teleportPlayer(event.getPlayer(), destination);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if (event.getChangedType() == Material.PORTAL)
			event.setCancelled(true);
	}
}
