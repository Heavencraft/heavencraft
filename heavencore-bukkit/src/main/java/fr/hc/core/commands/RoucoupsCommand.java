package fr.hc.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

public class RoucoupsCommand extends AbstractCommandExecutor
{
	public RoucoupsCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "roucoups");
	}

	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (player.getAllowFlight())
		{
			player.setAllowFlight(false);
			ChatUtil.sendMessage(player, "Vous venez de descendre de {Roucoups}.");
		}
		else
		{
			player.setAllowFlight(true);
			ChatUtil.sendMessage(player, "{Roucoups} utilise vol.");
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande ne peut pas être utilisée depuis la {console}.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub
	}

}