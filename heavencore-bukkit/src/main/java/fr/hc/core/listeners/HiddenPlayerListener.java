package fr.hc.core.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class HiddenPlayerListener extends AbstractBukkitListener
{

	public HiddenPlayerListener(JavaPlugin plugin)
	{
		super(plugin);
	}

	private final List<String> hiddenPlayers = new ArrayList<String>();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		Player player = event.getPlayer();

		// Update how the player is seen
		if (hiddenPlayers.contains(player.getName()))
		{
			ChatUtil.sendMessage(player, "Vous êtes {invisible} !");

			for (Player p : Bukkit.getOnlinePlayers())
				p.hidePlayer(player);
		}
		else
		{
			for (Player p : Bukkit.getOnlinePlayers())
				p.showPlayer(player);
		}

		// Update who the player can see
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (hiddenPlayers.contains(p.getName()))
				player.hidePlayer(p);
			else
				player.showPlayer(p);
		}
	}

}
