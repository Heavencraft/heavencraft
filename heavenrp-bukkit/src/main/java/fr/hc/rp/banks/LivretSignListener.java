package fr.hc.rp.banks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.RPPermissions;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.users.RPUser;
import fr.hc.rp.exceptions.BankAccountNotFoundException;

public class LivretSignListener extends AbstractBankAccountSignListener implements Listener
{
	enum LivretProAction
	{
		DEPOSIT, WITHDRAW, STATEMENT
	}

	private final HeavenRP plugin = HeavenRPInstance.get();

	private final Map<UUID, LivretProAction> pendingActions = new HashMap<UUID, LivretProAction>();
	private final Map<UUID, Integer> selectedAccounts = new HashMap<UUID, Integer>();

	public LivretSignListener(JavaPlugin plugin)
	{
		super(plugin, "Livret", RPPermissions.LIVRETPRO_SIGN);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	protected void onConsultSignClick(Player player) throws HeavenException
	{
		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());

		final Map<BankAccount, String> accounts = plugin.getBankAccountProvider()
				.getAllPermissionnedBankAccountForUser(user);

		if (accounts.size() == 0)
			throw new HeavenException("{Trésorier} : Vous n'avez accès à aucun livret...");

		ChatUtil.sendMessage(player, "{Trésorier} : Voici la liste de vos livrets :");

		for (final Entry<BankAccount, String> account : accounts.entrySet())
			ChatUtil.sendMessage(player, "{%1$s} (%2$s) : {%3$s} pièces d'or", account.getKey().getId(),
					account.getValue(), account.getKey().getBalance());
	}

	@Override
	protected void onDepositSignClick(Player player) throws HeavenException
	{
		pendingActions.put(player.getUniqueId(), LivretProAction.DEPOSIT);

		onConsultSignClick(player);
		ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous déposer ?");
	}

	@Override
	protected void onWithdrawSignClick(Player player) throws HeavenException
	{
		pendingActions.put(player.getUniqueId(), LivretProAction.WITHDRAW);

		onConsultSignClick(player);
		ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous retirer ?");
	}

	@Override
	protected void onStatementSignClick(Player player) throws HeavenException
	{
		pendingActions.put(player.getUniqueId(), LivretProAction.STATEMENT);

		onConsultSignClick(player);
		ChatUtil.sendMessage(player, "{Trésorier} : De quel livret voulez-vous le relevé ?");
	}

	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		final UUID uniqueId = player.getUniqueId();

		final LivretProAction action = pendingActions.get(uniqueId);
		if (action == null)
			return;

		event.setCancelled(true);

		try
		{
			final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
			final int input = ConversionUtil.toUint(event.getMessage());

			final Integer accountId = selectedAccounts.get(uniqueId);
			if (accountId == null)
			{
				final BankAccount account = getAccount(user, input);

				switch (action)
				{
					case DEPOSIT:
						selectedAccounts.put(player.getUniqueId(), account.getId());
						ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous déposer ?");
						break;
					case WITHDRAW:
						selectedAccounts.put(player.getUniqueId(), account.getId());
						ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous retirer ?");
						break;
					case STATEMENT:
						player.getInventory().addItem(createLastTransactionsBook(account, 3));
						clear(uniqueId);
						ChatUtil.sendMessage(player, "{Trésorier} : L'opération a été effectuée avec succès.");
						break;
				}
			}
			else
			{
				executeTransaction(player, user, accountId, action, input);
				clear(uniqueId);
			}
		}
		catch (final HeavenException ex)
		{
			clear(uniqueId);
			ChatUtil.sendMessage(player, ex.getMessage());
		}
	}

	private BankAccount getAccount(RPUser user, int id) throws HeavenException
	{
		final BankAccount account = plugin.getBankAccountProvider().getBankAccountById(id);

		if (user.getBankAccountId() == id)
			return account;

		final Optional<Company> optCompany = plugin.getCompanyProvider().getOptionalCompanyByBankAccount(account);
		if (optCompany.isPresent() && optCompany.get().isEmployer(user))
			return account;

		final Optional<Town> optTown = plugin.getTownProvider().getOptionalTownByBankAccount(account);
		if (optTown.isPresent() && optTown.get().isMayor(user))
			return account;

		throw new BankAccountNotFoundException(id);
	}

	private void executeTransaction(Player player, RPUser user, int accountId, LivretProAction action, int delta)
			throws HeavenException
	{
		final BankAccount bank = plugin.getBankAccountProvider().getBankAccountById(accountId);

		Object from, to;
		switch (action)
		{
			case DEPOSIT:
				from = user;
				to = bank;
				break;
			case WITHDRAW:
				from = bank;
				to = user;
				break;
			default:
				throw new HeavenException("Opération invalide.");
		}

		new BankAccountMoneyTransfertQuery(from, to, delta)
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "{Trésorier} : L'opération a été effectuée avec succès.");
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();

	}

	private void clear(UUID uniqueId)
	{
		pendingActions.remove(uniqueId);
		selectedAccounts.remove(uniqueId);
	}
}