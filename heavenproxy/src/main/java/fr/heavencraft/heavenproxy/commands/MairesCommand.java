package fr.heavencraft.heavenproxy.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.exceptions.DatabaseErrorException;
import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class MairesCommand extends HeavenCommand
{
	private static final String QUERY = "SELECT u.name AS user, t.name AS town FROM users u, towns t, towns_users tu WHERE t.id = tu.town_id AND u.id = tu.user_id;";

	public MairesCommand()
	{
		super("maires", "", new String[] {});
	}

	@Override
	protected void onCommand(CommandSender sender, String[] args) throws HeavenException
	{
		try (Connection connection = plugin.getSemirpConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			final ResultSet rs = ps.executeQuery();

			if (rs.first())
			{
				Utils.sendMessage(sender, "Liste des maires connectés :");
				final ProxyServer proxy = ProxyServer.getInstance();

				do
				{
					if (proxy.getPlayer(rs.getString("user")) != null)
						Utils.sendMessage(sender, "- " + rs.getString("user") + " ({" + rs.getString("town") + "})");
				}
				while (rs.next());
			}
			else
				Utils.sendMessage(sender, "Aucun maire n'est connecté");

		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new DatabaseErrorException();
		}
	}
}