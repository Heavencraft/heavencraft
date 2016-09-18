package fr.hc.rp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.worlds.WorldManager;

public class SpawnCommand extends AbstractCommandExecutor
{
	private static final String SUCCESS_MESSAGE = "Vous avez été téléporté au spawn.";

	public SpawnCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "spawn");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			sendUsage(player);
			return;
		}

		PlayerUtil.teleportPlayer(player, WorldManager.getSpawn());
		ChatUtil.sendMessage(player, SUCCESS_MESSAGE);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "C'est si compliqué de faire /spawn !?");
	}
}