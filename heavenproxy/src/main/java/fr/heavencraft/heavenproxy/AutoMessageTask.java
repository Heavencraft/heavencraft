package fr.heavencraft.heavenproxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import fr.heavencraft.heavenproxy.exceptions.SQLErrorException;

public class AutoMessageTask implements Runnable
{
	private static final int PERIOD = 300;
	private static final String PREFIX = "§b[Heavencraft]§r ";
	private static final String GET_AUTO_MESSAGE = "SELECT text FROM automessages ORDER BY RAND() LIMIT 1;";

	public AutoMessageTask()
	{
		ProxyServer.getInstance().getScheduler().schedule(HeavenProxy.getInstance(), this, PERIOD, PERIOD, TimeUnit.SECONDS);
	}

	@Override
	public void run()
	{
		try
		{
			ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(PREFIX + getRandomMessage()));
		}
		catch (final SQLErrorException ex)
		{
			ex.printStackTrace();
		}
	}

	private static String getRandomMessage() throws SQLErrorException
	{
		try (final PreparedStatement ps = HeavenProxy.getConnection().prepareStatement(GET_AUTO_MESSAGE))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				if (!rs.next())
					throw new SQLErrorException();

				return rs.getString("text");
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}
}
