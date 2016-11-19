package fr.heavencraft.heavenproxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import fr.hc.core.exceptions.DatabaseErrorException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class AutoMessageTask implements Runnable
{
	private static final int PERIOD = 300;
	private static final String PREFIX = "§b[Heavencraft]§r ";
	private static final String GET_AUTO_MESSAGE = "SELECT text FROM automessages ORDER BY RAND() LIMIT 1;";

	private final HeavenProxy plugin = HeavenProxyInstance.get();

	public AutoMessageTask()
	{
		ProxyServer.getInstance().getScheduler().schedule(plugin, this, PERIOD, PERIOD, TimeUnit.SECONDS);
	}

	@Override
	public void run()
	{
		if (ProxyServer.getInstance().getPlayers().isEmpty())
			return;

		try
		{
			ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(PREFIX + getRandomMessage()));
		}
		catch (final DatabaseErrorException ex)
		{
			ex.printStackTrace();
		}
	}

	private String getRandomMessage() throws DatabaseErrorException
	{
		try (final Connection connection = plugin.getConnectionProvider().getConnection();
				final PreparedStatement ps = connection.prepareStatement(GET_AUTO_MESSAGE))
		{
			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new DatabaseErrorException();

			return rs.getString("text");
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new DatabaseErrorException();
		}
	}
}