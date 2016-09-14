package fr.hc.core.utils.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.chat.ChatMessage;

public class ChatUtil
{
	public static void sendMessage(CommandSender sender, String message, Object... args)
	{
		new SendMessageTask(sender, message, args).schedule();
	}
	
	public static void sendMessage(Player reciever, ChatMessage msg)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + reciever.getName() + " " + msg.build());
	}
}