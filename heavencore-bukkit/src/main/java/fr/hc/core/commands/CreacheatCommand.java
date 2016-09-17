package fr.hc.core.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.PlayerUtil;
import fr.hc.core.utils.chat.ChatUtil;

public class CreacheatCommand extends AbstractCommandExecutor
{

	public CreacheatCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "creacheat");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (player.getGameMode() == GameMode.CREATIVE)
		{
			player.setGameMode(GameMode.SURVIVAL);
		}
		else
		{
			player.setGameMode(GameMode.CREATIVE);
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			return;
		}
		Player player = PlayerUtil.getPlayer(args[0]);
		if (player.getGameMode() == GameMode.CREATIVE)
		{
			player.setGameMode(GameMode.SURVIVAL);
		}
		else
		{
			player.setGameMode(GameMode.CREATIVE);
		}

		ChatUtil.sendMessage(sender, "{%1$s} est maintenant en mode {%2$s}.", player.getName(),
				player.getGameMode().name());
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}
