package fr.heavencraft.heavenproxy.servers;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;

public class Server
{
	private static final Map<String, Server> serversByName = new HashMap<String, Server>();

	// Production environment
	public static final Server SemiRP = new Server("semirp", "SRP", "Monde Semi-RP");
	public static final Server Creative = new Server("creative", "Créa", "Monde Créatif");
	public static final Server Robinson = new Server("robinson", "1.11", "Le Robinson");
	// Testing environment
	public static final Server UAT_SemiRP = new Server("uat-semirp", "UAT", "UAT Semi-RP");
	public static final Server UAT_Creative = new Server("uat-creative", "UAT", "UAT Créatif");
	// Unknown server
	public static final Server UnknownServer = new Server("", "???", "Monde ???");

	private final String name;
	private final String chatPrefix;
	private final Title title;

	private Server(String name, String chatPrefix, String title)
	{
		serversByName.put(name, this);

		this.name = name;
		this.chatPrefix = chatPrefix;

		this.title = ProxyServer.getInstance().createTitle();
		this.title.title(new TextComponent(title));
	}

	public String getName()
	{
		return name;
	}

	public String getChatPrefix()
	{
		return chatPrefix;
	}

	public Title getTitle()
	{
		return title;
	}

	public static Server getUniqueInstanceByName(String name)
	{
		final Server server = serversByName.get(name);
		return server != null ? server : UnknownServer;
	}
}