package fr.heavencraft.heavenproxy.radio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class HcsCommand extends HeavenCommand
{
	public HcsCommand()
	{
		super("hcs", "heavencraft.commands.hcs", new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(sender);
			return;
		}

		String message = Utils.ArrayToString(args, 0, " ");

		final Matcher matcher = Pattern.compile("\\&([0-9A-Fa-f])").matcher(message);
		message = matcher.replaceAll("§$1");

		ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText("§b[Heavencraft Studio]§r " + message));
	}

	private void sendUsage(CommandSender sender)
	{
		Utils.sendMessage(sender, "/{hcs} <message>");
	}
}