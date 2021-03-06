package fr.hc.core.cmd.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class TpCommand extends AbstractCommandExecutor
{

	public TpCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "tp", CorePermissions.TP_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		Player toPlayer;

		switch (args.length)
		{
			case 1:
				toPlayer = PlayerUtil.getPlayer(args[0]);
				if (toPlayer == null)
					throw new HeavenException("Le joueur {%1$s} n'est pas connecté ou n'existe pas.", args[0]);

				player.teleport(toPlayer);
				ChatUtil.sendMessage(player, "Vous venez de vous téléporter sur {%1$s}", args[0]);
				break;

			case 2:
				toPlayer = PlayerUtil.getPlayer(args[0]);
				Player fromPlayer = PlayerUtil.getPlayer(args[0]);
				toPlayer = PlayerUtil.getPlayer(args[1]);

				if (fromPlayer == null || toPlayer == null)
					throw new HeavenException("Le joueur {%1$s} et/ou {%2$s} n'est pas connecté ou n'existe pas.",
							args[0], args[1]);

				fromPlayer.teleport(toPlayer);
				ChatUtil.sendMessage(fromPlayer, "Vous venez de vous téléporter sur {%1$s}", args[1]);
				ChatUtil.sendMessage(toPlayer, "{%1$s} a été téléporté sur vous.", args[0]);
				break;
			default:
				sendUsage(player);
				return;
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 2:
				Player fromPlayer = PlayerUtil.getPlayer(args[0]);
				Player toPlayer = PlayerUtil.getPlayer(args[1]);

				if (fromPlayer == null || toPlayer == null)
					throw new HeavenException("Le joueur {%1$s} et/ou {%2$s} n'est pas connecté ou n'existe pas.",
							args[0], args[1]);

				fromPlayer.teleport(toPlayer);
				ChatUtil.sendMessage(sender, "{%1$s} a été téléporté sur {%2$s}", args[0], args[1]);
				break;
			default:
				sendUsage(sender);
				return;
		}

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{tp} <joueur>");
		ChatUtil.sendMessage(sender, "/{tp} <joueur1> <joueur2>");
	}

}
