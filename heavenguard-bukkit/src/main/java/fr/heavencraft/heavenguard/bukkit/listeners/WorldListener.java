package fr.heavencraft.heavenguard.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.db.GlobalRegion;

public class WorldListener extends AbstractBukkitListener
{
	private final BukkitHeavenGuard plugin;

	protected WorldListener(BukkitHeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler
	private void onWorldLoad(WorldLoadEvent event)
	{
		final GlobalRegion world = plugin.getRegionProvider().getGlobalRegion(event.getWorld().getName());

		log.info("Loaded %1$s", world);
	}
}