package fr.hc.core.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.exceptions.HeavenException;

public abstract class SubCommand
{
	private final String permission;

	protected SubCommand()
	{
		this.permission = null;
	}

	protected SubCommand(String permission)
	{
		this.permission = permission;
	}

	public boolean canExecute(CommandSender sender)
	{
		return permission == null || sender.hasPermission(permission);
	}

	public abstract void onPlayerCommand(Player player, String[] args) throws HeavenException;

	public abstract void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException;

	public abstract void sendUsage(CommandSender sender);
}