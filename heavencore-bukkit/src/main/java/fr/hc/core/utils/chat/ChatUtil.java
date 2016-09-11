package fr.hc.core.utils.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil
{
	private static final String BEGIN = "{";
	private static final String END = "}";
	
	static String errorColor = ChatColor.RED.toString();
	static String normalColor = ChatColor.GOLD.toString();
	
	public static void sendMessage(CommandSender sender, String message, Object... args)
	{
		String format = ChatUtil.normalColor
				+ message.replace(BEGIN, ChatUtil.errorColor).replace(END, ChatUtil.normalColor);
		sender.sendMessage(String.format(format, args));
	}
}