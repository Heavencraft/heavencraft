package fr.hc.guard.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.guard.HeavenGuard;
import fr.hc.guard.HeavenGuardInstance;

public abstract class HeavenGuardSubCommand extends SubCommand
{
	protected final HeavenGuard plugin = HeavenGuardInstance.get();

	public HeavenGuardSubCommand(String permission)
	{
		super(permission);
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(sender);
			return;
		}

		final String regionName = args[0].toLowerCase();

		final String[] args2 = new String[args.length - 1];

		if (args2.length != 0)
		{
			for (int i = 0; i != args2.length; i++)
				args2[i] = args[i + 1].toLowerCase();
		}

		execute(sender, regionName, args2);
	}

	public abstract void execute(CommandSender sender, String regionName, String[] args) throws HeavenException;

	@Override
	public abstract void sendUsage(CommandSender sender);
}