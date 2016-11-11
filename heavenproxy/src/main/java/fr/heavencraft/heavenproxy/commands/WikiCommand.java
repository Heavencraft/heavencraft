package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import fr.heavencraft.heavenproxy.Utils;
import fr.hc.core.exceptions.HeavenException;

public class WikiCommand extends HeavenCommand
{
	public WikiCommand()
	{
		super("wiki", null, new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		Utils.sendMessage(sender, ChatColor.GREEN + "Plus d'informations sur notre wiki:");
		Utils.sendMessage(sender, ChatColor.GREEN + "http://wiki.heavencraft.fr/");
	}

}
