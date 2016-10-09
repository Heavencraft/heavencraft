package fr.heavencraft.heavenguard.bukkit.commands.owner;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.guard.cmd.HeavenGuardSubCommand;
import fr.hc.guard.db.regions.Region;

abstract class AbstractOwnerSubCommand extends HeavenGuardSubCommand
{
	protected AbstractOwnerSubCommand(String permission)
	{
		super(permission);
	}

	@Override
	public boolean canExecute(CommandSender sender, String[] args)
	{
		if (super.canExecute(sender, args))
			return true;

		if (args == null || args.length == 0)
			return false;

		return canExecute(sender, args[0]);
	}

	public boolean canExecute(CommandSender sender, String regionName)
	{
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