package fr.hc.rp.banks;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.users.RPUser;

public class LivretSignListener extends AbstractBankAccountSignListener implements Listener
{
	private final Collection<String> deposants = new HashSet<String>();
	private final Collection<String> retirants = new HashSet<String>();
	private final BukkitHeavenRP plugin;

	public LivretSignListener(BukkitHeavenRP plugin)
	{
		super(plugin, "Livret", "heavencraft.signs.livret");
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@Override
	protected void onConsultSignClick(Player player) throws HeavenException
	{
		Optional<RPUser> user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (!user.isPresent())
			throw new HeavenException(
					"Votre UUID n'est pas associé a un compte Heavencraft. Contactez un administrateur.");
		BankAccount account = plugin.getBankAccountProvider().getBankAccountByUser(user.get());

		ChatUtil.sendMessage(player, "{Trésorier} : Vous avez {%1$d} pièces d'or sur votre livret.",
				account.getBalance());
	}

	@Override
	protected void onDepositSignClick(Player player) throws HeavenException
	{
		ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous déposer ?");
		deposants.add(player.getName());
	}

	@Override
	protected void onWithdrawSignClick(Player player) throws HeavenException
	{
		ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous retirer ?");
		retirants.add(player.getName());
	}

	@Override
	protected void onStatementSignClick(Player player) throws HeavenException
	{
		Optional<RPUser> user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		if (!user.isPresent())
			throw new HeavenException(
					"Votre UUID n'est pas associé a un compte Heavencraft. Contactez un administrateur.");
		BankAccount account = plugin.getBankAccountProvider().getBankAccountByUser(user.get());

		player.getInventory().addItem(createLastTransactionsBook(account, 2));
	}

	// private static String buildTransactionLog(Player player, boolean isDepot)
	// {
	// return (isDepot ? "Dépot de " : "Retrait de ") + player.getName();
	// }

	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		String playerName = player.getName();
		boolean isDepot = false;

		if (deposants.contains(playerName))
		{
			deposants.remove(playerName);
			isDepot = true;
		}
		else if (retirants.contains(playerName))
		{
			retirants.remove(playerName);
			isDepot = false;
		}
		else
		{
			// The player is not withdrawing or depositing.
			return;
		}

		event.setCancelled(true);

		try
		{
			int delta = Integer.parseInt(event.getMessage());
			if (delta <= 0)
				throw new HeavenException("Le nombre {%1$s} doit être positif.", delta);

			Optional<RPUser> user = plugin.getUserProvider().getUserByUniqueId(event.getPlayer().getUniqueId());
			if (!user.isPresent())
				throw new HeavenException(
						"Votre UUID n'est pas associé a un compte Heavencraft. Contactez un administrateur.");

			BankAccount bank = plugin.getBankAccountProvider().getBankAccountByUser(user.get());

			try
			{
				if (isDepot)
					new BankAccountMoneyTransfertQuery(user.get(), bank, delta).executeQuery();
				else
					new BankAccountMoneyTransfertQuery(bank, user.get(), delta).executeQuery();

				ChatUtil.sendMessage(player, "{Trésorier} : L'opération a bien été effectuée.");
			}
			catch (SQLException e)
			{
				throw new HeavenException(e.getMessage());
			}
		}
		catch (HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
		}
	}
}
