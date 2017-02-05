package fr.heavencraft.heavenguard.bukkit.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuard;

public class TeleportFlagListener extends AbstractBukkitListener
{
	private static final String TELEPORT_NOT_POSSIBLE = "Une force inconnue semble vous retenir.";

	private final HeavenGuard plugin;

	public TeleportFlagListener(BukkitHeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	private void onPlayerTeleport(PlayerTeleportEvent event)
	{
		final Location from = event.getFrom();
		if (!plugin.getRegionManager().canTeleport(from.getWorld().getName(), from.getBlockX(), from.getBlockY(), from.getBlockZ()))
		{
			ChatUtil.sendMessage(event.getPlayer(), TELEPORT_NOT_POSSIBLE);
			event.setCancelled(true);
			return;
		}

		final Location to = event.getTo();
		if (!plugin.getRegionManager().canTeleport(to.getWorld().getName(), to.getBlockX(), to.getBlockY(), to.getBlockZ()))
		{
			ChatUtil.sendMessage(event.getPlayer(), TELEPORT_NOT_POSSIBLE);
			event.setCancelled(true);
			return;
		}
	}
}