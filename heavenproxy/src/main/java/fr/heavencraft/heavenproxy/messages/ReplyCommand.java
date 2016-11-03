package fr.heavencraft.heavenproxy.messages;

import net.md_5.bungee.api.CommandSender;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class ReplyCommand extends HeavenCommand
{
	public ReplyCommand()
	{
		super("reply", null, new String[] { "r" });
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

		TellCommand.reply(sender, message);
	}

	private static void sendUsage(CommandSender sender)
	{
		Utils.sendMessage(sender, "/{r} <message> : répond en dernier message privé reçu.");
	}
}