package fr.heavencraft.heavenproxy.commands;

import fr.hc.core.exceptions.HeavenException;
import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.HeavenProxyInstance;
import fr.heavencraft.heavenproxy.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public abstract class HeavenCommand extends Command
{
	protected final HeavenProxy plugin = HeavenProxyInstance.get();

	public HeavenCommand(String name, String permission, String[] aliases)
	{
		super(name, permission, aliases);

		plugin.getProxy().getPluginManager().registerCommand(plugin, this);
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		try
		{
			onCommand(sender, args);
		}
		catch (final HeavenException ex)
		{
			Utils.sendMessage(sender, ex.getMessage());
		}
	}

	protected abstract void onCommand(CommandSender sender, String[] args) throws HeavenException;
}