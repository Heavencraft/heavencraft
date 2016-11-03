package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class MeCommand extends HeavenCommand
{
	private final static String ME_MESSAGE = " * %1$s %2$s";

	public MeCommand()
	{
		super("me", "", new String[] { "minecraft:me" });
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(sender);
			return;
		}

		final String message = String.format(ME_MESSAGE, sender.getName(), Utils.ArrayToString(args, 0, " "));
		ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(message));
	}

	private void sendUsage(CommandSender sender)
	{
		Utils.sendMessage(sender, "/{me} <message>");
	}
}