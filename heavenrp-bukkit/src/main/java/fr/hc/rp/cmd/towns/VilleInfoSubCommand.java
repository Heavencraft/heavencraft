package fr.hc.rp.cmd.towns;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.InputUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.towns.Town;

public class VilleInfoSubCommand extends SubCommand
{
	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(sender);
			return;
		}

		final String townName = args[0];
		final Town town = HeavenRPInstance.get().getTownProvider().getTownByName(townName);

		ChatUtil.sendMessage(sender, "Nom : %1$s", town.getName());
		ChatUtil.sendMessage(sender, "Maires : %1$s", InputUtil.userIdsToString(town.getMayors(), ", "));
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{ville} info <nom de la ville>");
	}
}