package fr.heavencraft.heavenproxy.kick;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.chat.DisconnectReasonManager;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.listeners.LogListener;

public class KickCommand extends HeavenCommand
{
	private final static String KICK_MESSAGE = "Vous avez été exclu du serveur par %1$s :\n\n%2$s";

	public KickCommand()
	{
		super("kick", "heavencraft.commands.kick", new String[] { "gkick" });
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ProxiedPlayer player;
		String reason;

		switch (args.length)
		{
			case 0:
				sendUsage(sender);
				return;
			case 1:
				player = Utils.getPlayer(args[0]);
				reason = "";
				break;
			default:
				player = Utils.getPlayer(args[0]);
				reason = Utils.ArrayToString(args, 1, " ");
				break;
		}

		kickPlayer(player, sender.getName(), reason);
	}

	public static void kickPlayer(ProxiedPlayer player, String kickedBy, String reason)
	{
		LogListener.addKick(player.getName(), kickedBy, reason);

		DisconnectReasonManager.addKick(player.getName(), kickedBy, reason);
		Utils.kickPlayer(player, String.format(KICK_MESSAGE, kickedBy, reason));
	}

	private static void sendUsage(CommandSender sender)
	{
		Utils.sendMessage(sender, "/{kick} <joueur>");
		Utils.sendMessage(sender, "/{kick} <joueur> <raison>");
	}
}