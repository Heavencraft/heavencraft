package fr.heavencraft.heavenguard.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuard;
import fr.hc.guard.db.GlobalRegion;

public class WorldListener extends AbstractBukkitListener
{
	private final HeavenGuard plugin;

	protected WorldListener(BukkitHeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler
	private void onWorldLoad(WorldLoadEvent event)
	{
		final String world = event.getWorld().getName();

		GlobalRegion worldRegion = plugin.getGlobalRegionProvider().getGlobalRegion(world);
		if (worldRegion != null)
		{
			log.info("World region {} already exists.", world);
			return;
		}

		try
		{
			worldRegion = plugin.getGlobalRegionProvider().createGlobalRegion(world);
			log.info("Created world region {}", worldRegion);
		}
		catch (final HeavenException ex)
		{
			log.error("Unable to retrieve or create world region for {}, shutting down.", world, ex);
			Bukkit.shutdown();
		}
	}
}