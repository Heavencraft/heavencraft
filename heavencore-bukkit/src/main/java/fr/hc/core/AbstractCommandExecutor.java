package fr.hc.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public abstract class AbstractCommandExecutor implements CommandExecutor
{
	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final AbstractBukkitPlugin plugin;

	public AbstractCommandExecutor(AbstractBukkitPlugin plugin, String name)
	{
		this(plugin, name, "", new ArrayList<String>());
	}

	public AbstractCommandExecutor(AbstractBukkitPlugin plugin, String name, String permission)
	{
		this(plugin, name, permission, new ArrayList<String>());
	}

	public AbstractCommandExecutor(AbstractBukkitPlugin plugin, String name, List<String> aliases)
	{
		this(plugin, name, "", aliases);
	}

	public AbstractCommandExecutor(AbstractBukkitPlugin plugin, String name, String permission, List<String> aliases)
	{
		this.plugin = plugin;

		final PluginCommand command = plugin.getCommand(name);
		command.setExecutor(this);
		command.setPermission(permission);
		command.setPermissionMessage("");

		log.info("Command {} registered", name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		try
		{
			if (sender instanceof Player)
				onPlayerCommand((Player) sender, args);
			else
				onConsoleCommand(sender, args);
		}

		catch (final HeavenException ex)
		{
			ChatUtil.sendMessage(sender, ex.getMessage());
		}

		return true;
	}

	protected abstract void onPlayerCommand(Player player, String[] args) throws HeavenException;

	protected abstract void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException;

	protected abstract void sendUsage(CommandSender sender);
}