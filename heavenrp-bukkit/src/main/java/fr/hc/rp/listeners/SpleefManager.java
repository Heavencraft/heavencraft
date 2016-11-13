package fr.hc.rp.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.exceptions.HeavenException;

public class SpleefManager extends AbstractBukkitListener
{

	private static boolean isFighting = false;
	private static List<Player> fighters = new ArrayList<Player>();
	private static List<Location> spawns = new ArrayList<Location>();

	public SpleefManager(AbstractBukkitPlugin plugin)
	{

		super(plugin);
	}

	public static void startBattle(List<Player> players) throws HeavenException
	{
		if (isFighting)
			throw new HeavenException("Un combat est déjà en cours !");

		if (players.size() == 0)
			throw new HeavenException("Il n'y a pas de joueurs !?");

		if (players.size() == 1)
			throw new HeavenException("AUTO FIGTH MODE ON !");

		fighters = players;
		String list = "";

		for (Player player : players)
		{
			list += (list == "" ? "" : ", ") + player.getName();
		}

		isFighting = true;

		spawnAllPlayers();

		broadcastMessage("Un combat vient de commencer !");
		broadcastMessage("Les combattants sont : %1$s.", list);
	}

	public static void stopBattle()
	{
		if (isFighting)
		{
			broadcastMessage("Le combat vient d'être interrompu !");
			fighters.clear();
			isFighting = false;
		}
	}

	public static void addSpawn(Location l)
	{
		spawns.add(l);
	}

	public static void resetSpawns()
	{
		spawns.clear();
	}

	private static void spawnAllPlayers() throws HeavenException
	{
		if (spawns.size() < fighters.size())
			throw new HeavenException("Il n'y a pas assez de spawn !");

		List<Location> availableSpawns = new ArrayList<Location>(spawns);
		Random r = new Random();

		for (Player fighter : fighters)
		{
			spawnPlayer(fighter, availableSpawns.remove(r.nextInt(availableSpawns.size())));
		}
	}

	private static void spawnPlayer(Player p, Location l)
	{
		p.getInventory().clear();

		p.getInventory().setBoots(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);

		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		p.setHealth(20);
		p.setFoodLevel(30);
		p.setFireTicks(0);

		p.teleport(l);
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event)
	{
		if (isFighting)
		{
			Player player = event.getEntity();

			if (fighters.contains(player))
			{
				event.setKeepLevel(true);

				broadcastMessage("%1$s vient de mourir ! il fini %2$dème !", player.getName(), fighters.size());
				fighters.remove(player);

				if (fighters.size() == 1)
				{
					broadcastMessage("Le combat vient de se terminer, le gagnant est %1$s.", fighters.get(0).getName());

					fighters.clear();
					isFighting = false;
				}
			}
		}
	}

	private static void broadcastMessage(String message, Object... args)
	{
		Bukkit.broadcastMessage(ChatColor.AQUA + " * " + String.format(message, args));
	}

	public static boolean isPlaying(Player p)
	{
		if (fighters.contains(p))
			return true;
		return false;
	}
}
