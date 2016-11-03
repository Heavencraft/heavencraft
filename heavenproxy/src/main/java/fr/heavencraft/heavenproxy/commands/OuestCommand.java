package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class OuestCommand extends HeavenCommand
{
	public OuestCommand()
	{
		super("ouest", null, new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsageModo(sender);
			return;
		}

		ProxiedPlayer player = Utils.getPlayer(args[0]);

		Utils.sendMessage(sender, "{%1$s} est dans le monde {%2$s}.", player.getName(), player.getServer().getInfo()
				.getName());
	}

	private void sendUsageModo(CommandSender sender)
	{
		Utils.sendMessage(sender, "{/ouest} <joueur>");
	}
}