package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import fr.heavencraft.heavenproxy.Utils;
import fr.hc.core.exceptions.HeavenException;

public class VoterCommand extends HeavenCommand
{
	public VoterCommand()
	{
		super("voter", null, new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		Utils.sendMessage(sender, ChatColor.GREEN + "Vous pouvez nous aider en votant et commentant :");
		Utils.sendMessage(sender, ChatColor.GREEN + "http://www.mcserv.org/Heavencraftfr_3002.html 1 fois/6h");
		Utils.sendMessage(sender, ChatColor.GREEN + "http://mc-topserv.net/top/serveur.php?serv=46 1 fois/j");
		Utils.sendMessage(sender, ChatColor.GREEN + "http://top-serveurs-minecraft.fr/fiche_serveur.php?id=1345");
		Utils.sendMessage(sender, ChatColor.GREEN + "http://www.serveursminecraft.org/serveur.php?id=104");
		Utils.sendMessage(sender, ChatColor.GREEN + "http://www.rpg-paradize.com/site-Heavencraft-104357");
	}

}
