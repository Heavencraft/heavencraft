package fr.hc.core.cmd.admin;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class TpposCommand extends AbstractCommandExecutor
{

	public TpposCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "tppos", CorePermissions.TPPOS_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 3)
		{
			sendUsage(player);
			return;
		}

		try
		{
			final int x = Integer.parseInt(args[0]);
			final int y = Integer.parseInt(args[1]);
			final int z = Integer.parseInt(args[2]);

			player.teleport(new Location(player.getWorld(), x, y, z));
			ChatUtil.sendMessage(player, "Vous avez été téléporté en x = {%1$d}, y = {%2$d}, z = {%3$d}.", x, y, z);
		}
		catch (final NumberFormatException ex)
		{
			throw new HeavenException("Au moins un des nombres écrit est incorrect.");
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tppos} <x> <y> <z>");
	}

}
