package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.Utils;
import fr.hc.core.exceptions.HeavenException;

public class TextCommand extends HeavenCommand
{
	public TextCommand()
	{
		super("text", "heavencraft.commands.text", new String[] { "d", "dire" });
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (!(sender instanceof ProxiedPlayer))
			return;

		ProxiedPlayer player = (ProxiedPlayer) sender;

		player.chat(Utils.ArrayToString(args, 0, " "));
	}
}
