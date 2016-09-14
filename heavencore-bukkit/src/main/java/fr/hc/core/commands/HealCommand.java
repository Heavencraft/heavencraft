package fr.hc.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class HealCommand extends AbstractCommandExecutor
{

	public HealCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "heal");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{

		switch (args.length)
		{
			case 0:
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				player.setSaturation(player.getFoodLevel());
				player.setFireTicks(0);

				break;
			case 1:
				if (PlayerUtil.getPlayer(args[0]) != null)
				{
					PlayerUtil.getPlayer(args[0]).setHealth(player.getMaxHealth());
					PlayerUtil.getPlayer(args[0]).setFoodLevel(20);
					PlayerUtil.getPlayer(args[0]).setSaturation(player.getFoodLevel());
					PlayerUtil.getPlayer(args[0]).setFireTicks(0);
				}
				else
				{
					throw new HeavenException("Le joueur {%1$s} n'existe pas ou n'est pas connecté.", args[0]);
				}
				break;
			default:
				sendUsage(player);
				return;
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			ChatUtil.sendMessage(sender, "/heal <player>");
			return;
		}

		Player player = PlayerUtil.getPlayer(args[0]);
		if (PlayerUtil.getPlayer(args[0]) != null)
		{
			PlayerUtil.getPlayer(args[0]).setHealth(player.getMaxHealth());
		}
		else
		{
			ChatUtil.sendMessage(sender, "Le joueur {%1$s} n'existe pas ou n'est pas connecté.", args[0]);
		}

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{heal} <joueur>");
	}

}
