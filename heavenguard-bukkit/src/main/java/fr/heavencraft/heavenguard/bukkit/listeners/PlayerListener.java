package fr.heavencraft.heavenguard.bukkit.listeners;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.db.regions.Region;

public class PlayerListener extends AbstractBukkitListener
{
	private final BukkitHeavenGuard plugin;

	public PlayerListener(BukkitHeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerInteract(PlayerInteractEvent event)
	{
		log.debug("onPlayerInteract {}", event);

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (event.getMaterial() != Material.ARROW)
			return;

		try
		{
			displayRegionAt(event.getPlayer(), event.getClickedBlock().getLocation());
		}
		catch (final HeavenException ex)
		{
			ChatUtil.sendMessage(event.getPlayer(), ex.getMessage());
		}
	}

	public void displayRegionAt(Player player, Location location) throws HeavenException
	{
		log.debug("displaying region at {} to {}", location, player);

		final String world = location.getWorld().getName();
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();

		final Collection<Region> regions = plugin.getRegionProvider().getRegionsAtLocation(world, x, y, z);

		if (regions.isEmpty())
		{
			ChatUtil.sendMessage(player, "Il n'y a aucune protection ici.");
		}

		else
		{
			final StringBuilder str = new StringBuilder("Liste des protections actives ici : ");

			for (final Iterator<Region> it = regions.iterator(); it.hasNext();)
			{
				str.append(it.next().getName());

				if (it.hasNext())
					str.append(", ");
			}

			ChatUtil.sendMessage(player, str.toString());
		}

		final User user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId()).get();

		final StringBuilder canYouBuild = new StringBuilder("Pouvez-vous construire ? ");
		canYouBuild.append(plugin.getRegionManager().canBuildAt(user, world, x, y, z) ? "Oui." : "Non.");
		ChatUtil.sendMessage(player, canYouBuild.toString());

		final StringBuilder pvpEnabled = new StringBuilder("PVP activé ? ");
		pvpEnabled.append(plugin.getRegionManager().isPvp(world, x, y, z) ? "Oui." : "Non.");
		ChatUtil.sendMessage(player, pvpEnabled.toString());
	}
}