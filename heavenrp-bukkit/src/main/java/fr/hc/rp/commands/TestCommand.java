package fr.hc.rp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;

/**
 * This command is a sandbox for new feature.
 *
 */
public class TestCommand extends AbstractCommandExecutor
{

	public TestCommand(AbstractBukkitPlugin plugin)
	{
		super(plugin, "test", "heavencraft.commands.test");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{

		return;
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		return;
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/test");
	}

}
