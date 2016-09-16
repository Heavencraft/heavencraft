package fr.hc.core.utils.chat;

import org.bukkit.command.CommandSender;

public class ChatUtil
{
	public static void sendMessage(CommandSender sender, String message, Object... args)
	{
		new SendMessageTask(sender, message, args).schedule();
	}
	
}