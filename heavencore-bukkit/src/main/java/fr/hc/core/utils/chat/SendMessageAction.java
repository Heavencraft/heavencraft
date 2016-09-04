package fr.hc.core.utils.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.sync.SyncTask;

class SendMessageTask implements SyncTask
{
	private static final String NORMAL_COLOR = ChatColor.GOLD.toString();
	private static final String HIGHLIGHT_COLOR = ChatColor.RED.toString();
	private static final String BEGIN = "{";
	private static final String END = "}";

	private final CommandSender sender;
	private final String message;
	private final Object[] args;

	public SendMessageTask(CommandSender sender, String message, Object... args)
	{
		this.sender = sender;
		this.message = message;
		this.args = args;
	}

	@Override
	public void execute() throws HeavenException
	{
		final String format = NORMAL_COLOR + message.replace(BEGIN, HIGHLIGHT_COLOR).replace(END, NORMAL_COLOR);
		sender.sendMessage(String.format(format, args));
	}
}