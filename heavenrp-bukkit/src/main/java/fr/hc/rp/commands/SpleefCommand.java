package fr.hc.rp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.WorldEditUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.RPPermissions;
import fr.hc.rp.listeners.SpleefManager;

public class SpleefCommand extends AbstractCommandExecutor
{
	public SpleefCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "Spleef", RPPermissions.SPLEEF);
	}

	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		if (args[0].equalsIgnoreCase("start"))
		{
			List<Player> players = new ArrayList<Player>();

			for (int i = 1; i != args.length; i++)
				players.add(PlayerUtil.getPlayer(args[i]));

			SpleefManager.startBattle(players);
		}
		else if (args[0].equalsIgnoreCase("startwe"))
		{
			Selection selection = WorldEditUtil.getWESelection(player);
			List<Player> players = new ArrayList<Player>();

			for (Player player2 : Bukkit.getOnlinePlayers())
				if (selection.contains(player2.getLocation()))
					players.add(player2);

			SpleefManager.startBattle(players);
		}
		else if (args[0].equalsIgnoreCase("stop"))
		{
			SpleefManager.stopBattle();
		}
		else if (args[0].equalsIgnoreCase("addspawn"))
		{
			SpleefManager.addSpawn(player.getLocation());
			ChatUtil.sendMessage(player, "Le point de spawn a été ajouté.");
		}
		else if (args[0].equalsIgnoreCase("resetspawn"))
		{
			SpleefManager.resetSpawns();
			ChatUtil.sendMessage(player, "Les points de spawn ont été supprimés.");
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "{/spleef} start <joueur1> <joueur2> etc. : démarre le combat");
		ChatUtil.sendMessage(sender,
				"{/spleef} startwe : démarre le combat avec les joueurs présent dans la sélection");
		ChatUtil.sendMessage(sender, "{/spleef} addspawn : ajoute un point de spawn");
		ChatUtil.sendMessage(sender, "{/spleef} resetspawn : retire tous les points de spawn");
		ChatUtil.sendMessage(sender, "{/spleef} stop");
	}
}
