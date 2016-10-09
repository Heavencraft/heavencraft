package fr.hc.core.cmd.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class TphereCommand extends AbstractCommandExecutor
{

	public TphereCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "tphere", CorePermissions.TPHERE_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final Player toTeleport = PlayerUtil.getPlayer(args[0]);

		toTeleport.teleport(player);

		ChatUtil.sendMessage(player, "Téléportation de {%1$s}.", toTeleport.getName());
		ChatUtil.sendMessage(toTeleport, "Vous avez été téléporté par {%1$s}.", player);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tphere} <joueur>");
	}

}
