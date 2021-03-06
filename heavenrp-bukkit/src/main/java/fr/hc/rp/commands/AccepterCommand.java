package fr.hc.rp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class AccepterCommand extends AbstractCommandExecutor
{
	public AccepterCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "accepter");
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

		if (!player.hasPermission(CorePermissions.ACCEPTER_COMMAND))
			throw new HeavenException("Vous n'avez actuellement pas la permission de faire /accepter.");

		if (!toTeleport.hasPermission(CorePermissions.REJOINDRE_COMMAND))
			throw new HeavenException("{%1$s} n'a actuellement pas la permission de faire /rejoindre.",
					toTeleport.getName());

		if (!RejoindreCommand.acceptRequest(toTeleport.getName(), player.getName()))
			throw new HeavenException("{%1$s} n'a pas demandé à vous rejoindre.", toTeleport.getName());

		// toTeleport.teleport(player);
		PlayerUtil.teleportPlayer(toTeleport, player);
		ChatUtil.sendMessage(toTeleport, " Vous avez été téléporté à {%1$s}.", player.getName());
		ChatUtil.sendMessage(player, "{%1$s} a été téléporté.", toTeleport.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{rejoindre} <joueur>");
		ChatUtil.sendMessage(sender, "/{accepter} <joueur>");
	}
}
