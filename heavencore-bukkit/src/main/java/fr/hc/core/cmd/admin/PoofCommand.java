package fr.hc.core.cmd.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.CorePermissions;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class PoofCommand extends AbstractCommandExecutor
{
	private final List<String> hiddenPlayers = new ArrayList<String>();

	public PoofCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "poof", CorePermissions.POOF_COMMAND);
		new AbstractBukkitListener(plugin)
		{
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
		};
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		String playerName = player.getName();

		if (hiddenPlayers.contains(player.getName()))
		{
			hiddenPlayers.remove(playerName);

			for (Player p : Bukkit.getOnlinePlayers())
				p.showPlayer(player);

			ChatUtil.sendMessage(player, "Vous êtes maintenant {visible} !");
		}
		else
		{
			hiddenPlayers.add(playerName);

			for (Player p : Bukkit.getOnlinePlayers())
				p.hidePlayer(player);

			ChatUtil.sendMessage(player, "Vous êtes maintenant {invisible} !");
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande ne peut pas être utilisée depuis la {console}.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}

}
