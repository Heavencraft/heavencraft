package fr.hc.rp.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import fr.hc.core.AbstractBukkitListener;
import fr.hc.core.db.users.balance.UpdateUserBalanceQuery;
import fr.hc.core.event.FirstLoginEvent;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.queries.BatchQuery;
import fr.hc.core.tasks.queries.Query;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.UpdateBankAccountBalanceQuery;
import fr.hc.rp.db.users.RPUser;

public class EconomyListener extends AbstractBukkitListener
{
	private final HeavenRP plugin;

	public EconomyListener(BukkitHeavenRP plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler
	private void onFirstLogin(FirstLoginEvent event) throws HeavenException
	{
		// Dirty cast, to rework
		final RPUser user = (RPUser) event.getUser();
		final Player player = event.getPlayer();

		final BankAccount account = plugin.getBankAccountProvider().getBankAccountByUser(user);
		final int benefit;

		if (account.getBalance() >= 25000)
			benefit = 25;
		else
			benefit = (int) (account.getBalance() * 0.001D);

		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateUserBalanceQuery(user, 5, plugin.getUserProvider()));
		if (benefit > 0)
		{
			queries.add(new UpdateBankAccountBalanceQuery(account, benefit));
			// queries.add(new AddTransactionQuery(account, benefit, "Intérets journaliers"));
		}

		new BatchQuery(queries)
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "Vous venez d'obtenir 5 pièces d'or en vous connectant !");

				if (benefit > 0)
				{
					ChatUtil.sendMessage(player, "Votre livret vous a rapporté %1$s pièces d'or.", benefit);
				}
			}
		}.schedule();
	}
}