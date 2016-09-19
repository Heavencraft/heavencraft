package fr.heavencraft.heavenguard.bukkit.commands.owner;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.bukkit.command.CommandSender;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class InfoSubCommand extends AbstractOwnerSubCommand
{
	public InfoSubCommand()
	{
		super(HeavenGuardPermissions.INFO_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		info(sender, regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {info} <protection>");
	}

	private void info(CommandSender sender, String name) throws HeavenException
	{
		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(name);
		if (!optRegion.isPresent())
			throw new RegionNotFoundException(name);

		final Region region = optRegion.get();

		ChatUtil.sendMessage(sender, "Protection : %1$s", region.getName());
		ChatUtil.sendMessage(sender, "Coordonnées : [{%1$s %2$s %3$s}] -> [{%4$s %5$s %6$s}] ({%7$s})", //
				region.getMinX(), region.getMinY(), region.getMinZ(), //
				region.getMaxX(), region.getMaxY(), region.getMaxZ(), //
				region.getWorld());

		/*
		 * Flags
		 */

		final String flags = region.getFlagHandler().toString();
		if (!flags.isEmpty())
			ChatUtil.sendMessage(sender, flags);

		final Optional<Region> parent = region.getParent();
		if (parent.isPresent())
			ChatUtil.sendMessage(sender, "Parent : %1$s", parent.get());

		final Collection<User> owners = region.getMembers(true);
		if (!owners.isEmpty())
		{
			final StringBuilder str = new StringBuilder();

			for (final Iterator<User> it = owners.iterator(); it.hasNext();)
			{
				str.append(it.next());

				if (it.hasNext())
					str.append(", ");
			}

			ChatUtil.sendMessage(sender, "Propriétaires : %1$s", str.toString());
		}

		final Collection<User> members = region.getMembers(false);
		if (!members.isEmpty())
		{
			final StringBuilder str = new StringBuilder();

			for (final Iterator<User> it = members.iterator(); it.hasNext();)
			{
				str.append(it.next());

				if (it.hasNext())
					str.append(", ");
			}

			ChatUtil.sendMessage(sender, "Membres : %1$s", str.toString());
		}
	}
}