package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.exceptions.HeavenException;

public class ModoCommand extends HeavenCommand {

	public ModoCommand()
	{
		super("modo", "", new String[] {});
	}

	private void sendUsageUser(CommandSender sender)
	{
		Utils.sendMessage(sender, "Besoin de l'aide d'un modérateur ?");
		Utils.sendMessage(sender, "{/modo} <Votre demande>");
	}

	private void sendUsageModo(CommandSender sender)
	{
		Utils.sendMessage(sender, "{/modo} Pour avoir la liste des requêtes que vous suivez.");
		Utils.sendMessage(sender, "{/modo} liste");
		Utils.sendMessage(sender, "{/modo} <joueur> : accepte une requête.");
	}



	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (!(sender instanceof ProxiedPlayer))
			return;
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		/*
		 * Modérateur
		 */
		
		if (sender.hasPermission("heavencraft.commands.modo"))
		{
			switch (args.length)
			{
			case 0:
				sendUsageModo(sender);
				break;

			case 1:
				if (args[0].equalsIgnoreCase("liste"))
					Utils.sendMessage(sender, HeavenProxy.getInstance().getRequestsManager().staffSendsRequete());
				
				else
					Utils.sendMessage(sender, HeavenProxy.getInstance().getRequestsManager().staffActionsRequete(
							Utils.getRealName(args[0]), sender.getName()));
				
				break;
				
			default:
				Utils.sendMessage(sender, "{* Tu cherche a faire quelle commande au juste ? (Troll)");
				return;
			}
		}
		
		/*
		 * Joueur
		 */
		
		else
		{
			switch (args.length)
			{
			case 0:
				sendUsageUser(sender);
				return;

			case 1:
			case 2:
			case 3:
				Utils.sendMessage(sender, "Votre requête doit contenir un minimum de {3 mots} et {1 verbe}.");
				return;

			default:
				String texte = "";
				for (int i = 0; i < args.length; i++)
					texte += args[i] + " ";

				Utils.sendMessage(sender, HeavenProxy.getInstance().getRequestsManager().ajouterRequete(sender.getName(), texte, player.getServer().getInfo().getName()));
			}
		}

		return;
	}


}
