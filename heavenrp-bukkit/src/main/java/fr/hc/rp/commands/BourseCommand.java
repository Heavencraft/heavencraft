package fr.hc.rp.commands;

import java.util.Optional;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.db.users.RPUser;

public class BourseCommand extends AbstractCommandExecutor
{
	private final static String PURSE_MESSAGE = "Vous comptez le nombre de pièces d'or dans votre bourse...";
	private final static String PURSE_EMPTY = "Malheureusement, elle est vide... :-(";
	private final static String PURSE_SUCCESS = "Fantastique ! Vous avez {%1$s} pièces d'or !";
	private final static String PURSE_FAIL = "Vous avez perdu le compte... Faites /bourse pour recompter.";

	private final Random random = new Random();
	private final HeavenRP plugin;

	public BourseCommand(BukkitHeavenRP plugin)
	{
		super(plugin, "bourse");

		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(player, PURSE_MESSAGE);

		final Optional<RPUser> user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (!user.isPresent())
			throw new HeavenException("Un problème inattendu s'est produit, merci de contacter un administrateur.");

		final int balance = user.get().getBalance();
		if (balance == 0)
		{
			ChatUtil.sendMessage(player, PURSE_EMPTY);
			return;
		}

		if (random.nextFloat() <= getFailPercentage(balance))
			ChatUtil.sendMessage(player, PURSE_FAIL);
		else
			ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
	}

	private float getFailPercentage(int balance)
	{
		if (balance < 100)
			return 0.1f;
		if (balance < 200)
			return 0.125f;
		if (balance < 500)
			return 0.143f;
		if (balance < 700)
			return 0.167f;
		if (balance < 1000)
			return 0.25f;

		return 0.34f;
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}