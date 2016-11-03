package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class DonCommand extends HeavenCommand
{
	public DonCommand()
	{
		super("don", null, new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		Utils.sendMessage(sender, ChatColor.GREEN + "Vous pouvez nous faire un don sur notre site, en bas de la page.");
		Utils.sendMessage(sender, ChatColor.GOLD + "http://heavencraft.fr/");
		Utils.sendMessage(sender, ChatColor.GREEN + "Nous supportons les moyens suivants:");
		Utils.sendMessage(sender, ChatColor.RED + "Carte Bancaire " + ChatColor.GREEN + "Utilisez le boutton: " + ChatColor.GOLD + "Faire un don");
		Utils.sendMessage(sender, ChatColor.RED + "Paypal " + ChatColor.GREEN + "Utilisez le boutton: " + ChatColor.GOLD + "Faire un don");
		Utils.sendMessage(sender, ChatColor.RED + "Starpass " + ChatColor.GREEN + "Utilisez le boutton: " + ChatColor.RESET + "Acheter des HPs via StarPass");
	}

}
