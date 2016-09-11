package fr.hc.rp.commands;

import java.util.Optional;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.AbstractCommandExecutor;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.bankaccounts.BankAccountProvider;
import fr.hc.rp.db.users.RPUser;

public class BourseCommand extends AbstractCommandExecutor
{

	private final static String PURSE_MESSAGE = "Vous comptez le nombre de pièces d'or dans votre bourse...";
	private final static String PURSE_EMPTY = "Malheureusement, elle est vide... :-(";
	private final static String PURSE_SUCCESS = "Fantastique ! Vous avez {%1$s} pièces d'or !";
	private final static String PURSE_FAIL = "Vous avez perdu le compte... Faites /bourse pour recompter.";

	private static Random Random = new Random();
	private final HeavenRP plugin = HeavenRPInstance.get();
	
	public BourseCommand(BukkitHeavenRP plugin)
	{
		super(plugin, "bourse");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(player, PURSE_MESSAGE);
		
		Optional<RPUser> user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		
		if(user.isPresent()) {
			final int balance = user.get().getBalance();
			if(balance == 0) {
				ChatUtil.sendMessage(player, PURSE_EMPTY);
			}
			else if (balance < 100) {
				if(Random.nextDouble() <= 0.1) {
					ChatUtil.sendMessage(player, PURSE_FAIL);
				}
				else {
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
			}
			else if (balance < 200) {
				if(Random.nextDouble() <= 0.125) {
					ChatUtil.sendMessage(player, PURSE_FAIL);
				}
				else {
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
			}
			else if (balance < 500) {
				if(Random.nextDouble() <= 0.143) {
					ChatUtil.sendMessage(player, PURSE_FAIL);
				}
				else {
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
			}
			else if (balance < 700) {
				if(Random.nextDouble() <= 0.167) {
					ChatUtil.sendMessage(player, PURSE_FAIL);
				}
				else {
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
			}
			else if (balance < 1000) {
				if(Random.nextDouble() <= 0.25) {
					ChatUtil.sendMessage(player, PURSE_FAIL);
				}
				else {
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
			}
			else {
				if(Random.nextDouble() <= 0.34) {
					ChatUtil.sendMessage(player, PURSE_FAIL);
				}
				else {
					ChatUtil.sendMessage(player, PURSE_SUCCESS, balance);
				}
			}
		}
		else {
			throw new HeavenException("Le joueur n'a pas de compte");
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}
