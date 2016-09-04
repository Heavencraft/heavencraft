package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.hc.guard.BukkitHeavenGuard;

public abstract class AbstractSubCommand implements SubCommand
{
	protected final BukkitHeavenGuard plugin;
	private final String permission;

	protected AbstractSubCommand(BukkitHeavenGuard plugin, String permission)
	{
		this.plugin = plugin;
		this.permission = permission;
	}

	@Override
	public boolean canExecute(CommandSender sender, String regionName)
	{
		return sender.hasPermission(permission);
	}
}