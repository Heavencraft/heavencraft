package fr.heavencraft.heavenproxy;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.heavencraft.heavenproxy.async.QueriesHandler;
import fr.heavencraft.heavenproxy.ban.BanCommand;
import fr.heavencraft.heavenproxy.ban.BanListener;
import fr.heavencraft.heavenproxy.ban.SilentBanListener;
import fr.heavencraft.heavenproxy.ban.UnbanCommand;
import fr.heavencraft.heavenproxy.chat.ChatListener;
import fr.heavencraft.heavenproxy.chat.FloodListener;
import fr.heavencraft.heavenproxy.chat.ModoListener;
import fr.heavencraft.heavenproxy.chat.TabCompleteListener;
import fr.heavencraft.heavenproxy.commands.ActifCommand;
import fr.heavencraft.heavenproxy.commands.DonCommand;
import fr.heavencraft.heavenproxy.commands.ListCommand;
import fr.heavencraft.heavenproxy.commands.MeCommand;
import fr.heavencraft.heavenproxy.commands.ModoCommand;
import fr.heavencraft.heavenproxy.commands.NexusCommand;
import fr.heavencraft.heavenproxy.commands.OuestCommand;
import fr.heavencraft.heavenproxy.commands.SayCommand;
import fr.heavencraft.heavenproxy.commands.SendCommand;
import fr.heavencraft.heavenproxy.commands.SpyCommand;
import fr.heavencraft.heavenproxy.commands.TextCommand;
import fr.heavencraft.heavenproxy.commands.VoterCommand;
import fr.heavencraft.heavenproxy.commands.WikiCommand;
import fr.heavencraft.heavenproxy.jit.ServerProcessManager;
import fr.heavencraft.heavenproxy.kick.KickCommand;
import fr.heavencraft.heavenproxy.kick.RagequitCommand;
import fr.heavencraft.heavenproxy.listeners.LogListener;
import fr.heavencraft.heavenproxy.listeners.SpyListener;
import fr.heavencraft.heavenproxy.managers.RequestsManager;
import fr.heavencraft.heavenproxy.messages.ReplyCommand;
import fr.heavencraft.heavenproxy.messages.TellCommand;
import fr.heavencraft.heavenproxy.motd.ProxyPingListener;
import fr.heavencraft.heavenproxy.mute.MuteCommand;
import fr.heavencraft.heavenproxy.mute.MuteListener;
import fr.heavencraft.heavenproxy.radio.HcsCommand;
import fr.heavencraft.heavenproxy.radio.RadioCommand;
import fr.heavencraft.heavenproxy.servers.TitleListener;
import fr.heavencraft.heavenproxy.users.UsersListener;
import fr.heavencraft.heavenproxy.warn.WarnCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class HeavenProxy extends Plugin
{
	private final ProxyLogger log = ProxyLogger.getLogger(getClass());

	private static String databaseUrl;

	private static Connection _connection;

	private static HeavenProxy _instance;

	private RequestsManager _requestsManager;

	@Override
	public void onEnable()
	{
		super.onEnable();
		_instance = this;

		try
		{
			final File file = new File(getDataFolder(), "config.yml");
			log.info("Loading configuration file %1$s", file);
			final Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

			final String username = configuration.getString("mysql.username");
			final String password = configuration.getString("mysql.password");
			final String database = configuration.getString("mysql.database");

			final StringBuilder builder = new StringBuilder();
			builder.append("jdbc:mysql://localhost:3306/").append(database);
			builder.append("?user=").append(username).append("&password=").append(password);
			builder.append("&zeroDateTimeBehavior=convertToNull");
			databaseUrl = builder.toString();

			log.info("Using database url %1$s", databaseUrl);

			new QueriesHandler();

			new LogListener();
			new SpyListener();

			new ActifCommand();
			new DonCommand();
			new ListCommand();
			new MeCommand();
			new ModoCommand();
			new NexusCommand();
			new OuestCommand();
			new ReplyCommand();
			new SayCommand();
			new SendCommand();
			new SpyCommand();
			new TellCommand();
			new TextCommand();
			new VoterCommand();
			new WikiCommand();

			// Ban
			new BanCommand();
			new BanListener();
			new SilentBanListener();
			new UnbanCommand();

			// Chat
			new ChatListener();
			new FloodListener();
			new ModoListener();
			new TabCompleteListener();

			// Kick
			new KickCommand();
			new RagequitCommand();

			// MOTD
			new ProxyPingListener();

			// Mute
			new MuteCommand();
			new MuteListener();

			// Radio
			new HcsCommand();
			new RadioCommand();

			// Servers
			new TitleListener();

			// Users
			new UsersListener();

			// Warn
			new WarnCommand();

			new ServerProcessManager();

			new AutoMessageTask();

			_requestsManager = new RequestsManager();

		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			ProxyServer.getInstance().stop();
		}
	}

	@Override
	public void onDisable()
	{
		super.onDisable();

		getProxy().getScheduler().cancel(this);
	}

	public RequestsManager getRequestsManager()
	{
		return _requestsManager;
	}

	public static Connection getConnection()
	{
		try
		{
			if (_connection == null || _connection.isClosed())
			{
				_connection = DriverManager.getConnection(databaseUrl);
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			ProxyServer.getInstance().stop();
		}

		return _connection;
	}

	public static HeavenProxy getInstance()
	{
		return _instance;
	}
}