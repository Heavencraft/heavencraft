package fr.hc.rp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class SpawnCommand extends AbstractCommandExecutor
{

	private static final String SUCCESS_MESSAGE = "Vous avez été téléporté au spawn.";

	private Location spawnLocation = new Location(Bukkit.getWorld("world"), -159, 64, 152);

	public SpawnCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "spawn");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		PlayerUtil.teleportPlayer(player, spawnLocation);
		ChatUtil.sendMessage(player, SUCCESS_MESSAGE);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}
}
