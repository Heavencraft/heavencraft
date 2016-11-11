package fr.heavencraft.heavenproxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import fr.heavencraft.heavenproxy.HeavenProxy;
import fr.heavencraft.heavenproxy.Utils;
import fr.hc.core.exceptions.HeavenException;

public abstract class HeavenCommand extends Command {
	
	protected HeavenProxy _plugin;
	
	public HeavenCommand(String name, String permission, String[] aliases)
	{
		super(name, permission, aliases);

		_plugin = HeavenProxy.getInstance();
		_plugin.getProxy().getPluginManager().registerCommand(_plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		try
		{
			onCommand(sender, args);
		}
		catch (HeavenException ex)
		{
			Utils.sendMessage(sender, ex.getMessage());
		}
	}
	
	protected abstract void onCommand(CommandSender sender, String[] args) throws HeavenException;
}
