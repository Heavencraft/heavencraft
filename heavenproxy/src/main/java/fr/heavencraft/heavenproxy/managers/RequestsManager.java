package fr.heavencraft.heavenproxy.managers;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import fr.heavencraft.heavenproxy.Utils;
import fr.heavencraft.heavenproxy.exceptions.PlayerNotConnectedException;

public class RequestsManager
{
	private final List<Request> _requetes = new ArrayList<Request>();

	public String ajouterRequete(String playerName, String request, String location)
	{
		final Request requete = getRequete(playerName);

		if (requete != null)
			return (ChatColor.DARK_GREEN + "[Requête] " + ChatColor.GREEN + "Vous avez déjà une requête en cours. Merci de bien vouloir patienter.");

		_requetes.add(new Request(playerName, request, location));
		sendStaff(ChatColor.DARK_GREEN + "[Requête] " + ChatColor.GREEN + playerName + " : " + request);

		return (ChatColor.DARK_GREEN + "[Requête] " + ChatColor.GREEN + "Votre requête a été envoyée à l'équipe. Une personne va vous répondre au plus vite.");
	}

	public String staffSendsRequete()
	{
		if (_requetes.size() == 0)
			return (ChatColor.DARK_GREEN + "[Requête] " + ChatColor.GREEN + "Il n'y a aucune requête.");

		String rep = (ChatColor.DARK_GREEN + "[Requête] " + ChatColor.GREEN + "Voici la liste des requêtes \nJoueurs Connectés:");
		String userDisco = "\nJoueurs Déconnectés:";
		for (final Request r : _requetes)
		{
			if (Utils.IsConnected(r.getPlayerName()))
				rep += ("\n " + ChatColor.WHITE + r.getPlayerName() + ChatColor.GREEN + " (" + r.getLocation()
						+ ") : " + r.getRequest());
			else
				userDisco += ("\n " + ChatColor.WHITE + r.getPlayerName() + ChatColor.GREEN + " ("
						+ r.getLocation() + ") : " + r.getRequest());
		}
		return (rep + userDisco);
	}

	public String staffActionsRequete(String playerName, String nameModo) throws PlayerNotConnectedException
	{
		final Request requete = getRequete(playerName);

		if (requete == null) // Si pas de reque à ce nom fini
			return ChatColor.GREEN + "Le joueur " + ChatColor.WHITE + playerName + ChatColor.GREEN
					+ " n'a pas fait de requête.";

		sendStaff(ChatColor.DARK_GREEN + "[Requête]} La requête de {" + playerName
				+ "} est prise en compte par {" + nameModo + "}.");
		_requetes.remove(requete);

		if (!Utils.IsConnected(playerName))
			return (ChatColor.GREEN + "Le joueur " + ChatColor.WHITE + playerName + ChatColor.GREEN + " est déconnecté. Il faut être plus rapide.");
		else
			Utils.sendMessage(Utils.getPlayer(playerName), ChatColor.DARK_GREEN + "[Requête]" + ChatColor.GREEN
					+ " Votre requête est prise en compte par " + ChatColor.WHITE + nameModo + ChatColor.GREEN
					+ ".");

		return (ChatColor.DARK_GREEN + "[Requête]" + ChatColor.GREEN
				+ "Tu es un bon modérateur TOI. D; (La requête de " + playerName + " a été supprimée) ("
				+ requete.getLocation() + ")");
		// p.sendMessage(ChatColor.DARK_GREEN + "[Requête] " + ChatColor.GREEN +
		// nameModo +
		// " a fermé votre requête, nous espérons que son aide vous a été utile.");
	}

	public Request getRequete(String playerName)
	{
		for (final Request r : _requetes)
			if (r.getPlayerName().equalsIgnoreCase(playerName))
				return r;

		return null;
	}

	private void sendStaff(String texte)
	{
		Utils.broadcast(
				texte.replace("{", ChatColor.WHITE.toString()).replace("}",
						ChatColor.GREEN.toString().replace("#", ChatColor.DARK_GREEN.toString())),
				"heavencraft.commands.modo");
	}

	private class Request
	{
		private final String _playerName;
		private final String _request;
		private final String _location;

		public Request(String playerName, String request, String location)
		{
			_playerName = playerName;
			_request = request;
			_location = location;
		}

		public String getPlayerName()
		{
			return _playerName;
		}

		public String getRequest()
		{
			return _request;
		}

		public String getLocation()
		{
			return _location;
		}
	}
}