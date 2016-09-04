package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.guard.BukkitHeavenGuard;
import fr.hc.guard.HeavenGuardPermissions;

public class RemoveSubCommand extends AbstractSubCommand
{
	public RemoveSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.REMOVE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			sendUsage(sender);
			return;
		}

		plugin.getRegionProvider().deleteRegion(regionName);
		ChatUtil.sendMessage(sender, "La protection {%1$s} a bien été supprimée.", regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {remove} <protection>");
	}
}