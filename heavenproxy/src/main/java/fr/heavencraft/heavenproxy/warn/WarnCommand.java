package fr.heavencraft.heavenproxy.warn;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.commands.HeavenCommand;
import fr.hc.core.exceptions.HeavenException;

public class WarnCommand extends HeavenCommand
{
	public WarnCommand()
	{
		super("warn", "heavencraft.commands.warn", new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(sender);
			return;
		}

		ProxiedPlayer player = Utils.getPlayer(args[0]);
		String warnType = args[1].toLowerCase();

		if (warnType.equals("sms"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous semblez écrire en SMS.");
			Utils.sendMessage(player,
					"Ce style d'écriture est déconseillé sur Heavencraft, car il vous fait perdre toute crédibilité.");
			Utils.sendMessage(player,
					"Faites un effort pour faciliter votre ouverture aux autres joueurs et éviter des sanctions.");

			Utils.sendMessage(sender, "Avertissement pour sms envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("maj"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous écrivez trop en majuscule.");
			Utils.sendMessage(player,
					"Ce style d'écriture est déconseillé sur Heavencraft, car il est l'équivalent de crier.");
			Utils.sendMessage(player,
					"Faites un effort pour faciliter votre ouverture aux autres joueurs et éviter des sanctions.");

			Utils.sendMessage(sender, "Avertissement pour majuscules envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("spam"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous écrivez de nombreux messages en peu de temps.");
			Utils.sendMessage(player,
					"Ce style de conversation est déconseillé sur Heavencraft, car il rend le fil de discussion illisible.");
			Utils.sendMessage(player,
					"Faites un effort pour faciliter votre ouverture aux autres joueurs et éviter des sanctions.");

			Utils.sendMessage(sender, "Avertissement pour spam envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("er"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous semblez avoir du mal avec é/er/ez.");
			Utils.sendMessage(player, " {-é} s'utilise si le verbe peut ętre remplacé par {fait}.");
			Utils.sendMessage(player, " {-er} s'utilise si le verbe peut ętre remplacé par {faire}.");
			Utils.sendMessage(player, " {-ez} s'utilise si le verbe peut ętre remplacé par {faites}.");

			Utils.sendMessage(sender, "Avertissement pour é/er/ez envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("se"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous semblez avoir du mal avec se/ce/sa/ça.");
			Utils.sendMessage(player, "Le {ce/ça} s'utilise s'il peut ętre remplacé par {cela}.");
			Utils.sendMessage(player, "Dans les autres cas, on utilise {se/sa}.");

			Utils.sendMessage(sender, "Avertissement pour se/ce envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("avoir"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous semblez avoir du mal avec le verbe avoir.");
			Utils.sendMessage(player,
					"Sa conjugaison est : J'ai / Tu as (T'as) / Il a / Nous avons /  Vous avez / Ils ont");

			Utils.sendMessage(sender, "Avertissement pour le verbe avoir envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("etre"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous semblez avoir du mal avec le verbe être.");
			Utils.sendMessage(player,
					"Sa conjugaison est : Je suis / Tu es (T'es) / Il est / Nous sommes / Vous êtes / Ils sont");

			Utils.sendMessage(sender, "Avertissement pour le verbe être envoyé à {" + player.getName() + "}.");
		}
		else if (warnType.equals("vouloir"))
		{
			Utils.sendMessage(player, " * {Attention !} Vous semblez avoir du mal avec le verbe vouloir.");
			Utils.sendMessage(player,
					"Sa conjugaison est : Je veux / Tu veux / Il veut / Nous voulons / Vous voulez / Ils veulent");

			Utils.sendMessage(sender, "Avertissement pour le verbe vouloir envoyé à {" + player.getName() + "}.");
		}
		else
			sendUsage(sender);
	}

	private static void sendUsage(CommandSender sender)
	{
		Utils.sendMessage(sender, "/{warn} <joueur> <alerte>");
		Utils.sendMessage(sender, "Alertes : sms, maj, er, se, avoir, etre, vouloir");
	}
}