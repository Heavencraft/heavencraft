package fr.heavencraft.heavenguard.bukkit.commands.owner;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.db.regions.Region;
import fr.heavencraft.heavenguard.bukkit.commands.AbstractSubCommand;

abstract class AbstractOwnerSubCommand extends AbstractSubCommand
{
	protected AbstractOwnerSubCommand(BukkitHeavenGuard plugin, String permission)
	{
		super(plugin, permission);
	}

	@Override
	public boolean canExecute(CommandSender sender, String regionName)
	{
		if (super.canExecute(sender, regionName))
			return true;

		if (sender instanceof Player)
		{
			final UUID uuid = ((Player) sender).getUniqueId();

			try
			{
				final Optional<? extends User> optUser = plugin.getUserProvider().getUserByUniqueId(uuid);
				if (!optUser.isPresent())
					return false;

				final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);
				if (!optRegion.isPresent())
					return false;

				return optRegion.get().isMember(optUser.get(), true);
			}
			catch (final HeavenException e)
			{
				return false;
			}
		}

		return false;
	}
}