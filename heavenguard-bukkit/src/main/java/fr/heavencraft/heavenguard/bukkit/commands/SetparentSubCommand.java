package fr.heavencraft.heavenguard.bukkit.commands;

import java.util.Optional;

import org.bukkit.command.CommandSender;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuardPermissions;
import fr.hc.guard.db.regions.Region;
import fr.hc.guard.db.regions.SetParentQuery;
import fr.hc.guard.exceptions.RegionNotFoundException;

public class SetparentSubCommand extends AbstractSubCommand
{
	public SetparentSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.SETPARENT_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Optional<Region> optRegion = plugin.getRegionProvider().getRegionByName(regionName);

		if (!optRegion.isPresent())
			throw new RegionNotFoundException(regionName);

		final Region region = optRegion.get();
		final Region parent;

		switch (args.length)
		{
			case 0: // Remove parent
				parent = null;
				break;

			case 1: // Set parent
				final Optional<Region> optParent = plugin.getRegionProvider().getRegionByName(args[0]);
				if (!optParent.isPresent())
					throw new RegionNotFoundException(args[0]);

				parent = optParent.get();
				break;

			default:
				sendUsage(sender);
				return;
		}

		new SetParentQuery(region, parent, plugin.getConnectionProvider())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(sender, "La protection {%1$s} a désormais pour parent {%2$s}.", region, parent);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(sender, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {setparent} <protection> <protection parente>");
	}
}