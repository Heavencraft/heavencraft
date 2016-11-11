package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.Utils;
import fr.hc.core.exceptions.HeavenException;

public class SendCommand extends HeavenCommand {
	
	private final static String FROM = "§6Vous avez reçu §c%1$s§6 de la part de §c%2$s§6.";
	private final static String TO = "§6Vous avez envoyé §c%1$s§6 à §c%2$s§6.";
	
	public SendCommand()
	{
		super("envoyer", null, new String[]{});
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length < 2)
		{
			sendUsage(sender);
			return;
		}
		
		ProxiedPlayer player = Utils.getPlayer(args[0]);
		String toSend = Utils.ArrayToString(args, 1, " ");
		
		sender.sendMessage(TextComponent.fromLegacyText(String.format(TO, toSend, player.getName())));
		player.sendMessage(TextComponent.fromLegacyText(String.format(FROM, toSend, sender.getName())));
	}
	
	private void sendUsage(CommandSender sender)
	{
		Utils.sendMessage(sender, "/{envoyer} <joueur> <quelque chose>");
	}
}
