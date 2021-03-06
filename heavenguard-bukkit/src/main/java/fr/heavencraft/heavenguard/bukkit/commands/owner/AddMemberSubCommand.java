package fr.heavencraft.heavenguard.bukkit.commands.owner;

import java.util.Optional;

import org.bukkit.command.CommandSender;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.db.regions.AddMemberQuery;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class AddMemberSubCommand extends AbstractOwnerSubCommand
{
	public AddMemberSubCommand()
	{
		super(HeavenGuardPermissions.ADDMEMBER_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);

		if (!optRegion.isPresent())
			throw new RegionNotFoundException(regionName);

		final Region region = optRegion.get();

		for (final String arg : args)
		{
			final Optional<? extends User> optUser = plugin.getUserProvider().getOptionalUserByName(arg);

			if (!optUser.isPresent())
				throw new UserNotFoundException(arg);

			final User user = optUser.get();

			new AddMemberQuery(region, user, false, plugin.getConnectionProvider())
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(sender, "{%1$s} est maintenant membre de la protection {%2$s}.", user, region);
				}

				@Override
				public void onException(HeavenException ex)
				{
					ChatUtil.sendMessage(sender, ex.getMessage());
				}
			}.schedule();
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {addmember} <protection> <membre(s)>");
	}
}