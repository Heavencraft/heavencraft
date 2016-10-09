package fr.hc.rp.commands;

import java.sql.SQLException;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.AbstractCommandExecutor;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.BukkitHeavenRP;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.db.bankaccounts.BankAccount;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.towns.Town;
import fr.hc.rp.db.users.RPUser;
import fr.hc.rp.exceptions.CompanyNotFoundException;
import fr.hc.rp.exceptions.TownNotFoundException;

public class PayerCommand extends AbstractCommandExecutor
{
	private final HeavenRP plugin;;

	public PayerCommand(BukkitHeavenRP plugin)
	{
		super(plugin, "payer");

		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final RPUser fromUser = plugin.getUserProvider().getOptionalUserByUniqueId(player.getUniqueId()).get();
		final BankAccount fromBankAccount = plugin.getBankAccountProvider().getBankAccountByUser(fromUser);
		BankAccount toBankAccount = null;
		final Town toTown;
		final RPUser toUser;
		final Company toCompany;
		final int delta;

		if (args.length != 3)
		{
			sendUsage(player);
			return;
		}

		try
		{
			delta = Integer.parseInt(args[2]);
			if (delta <= 0)
			{
				throw new HeavenException("Le nombre {%1$s} doit être positif.", delta);
			}
			if ((fromBankAccount.getBalance() - delta) < 0)
			{
				throw new HeavenException("Vous n'avez pas assez d'argent!");
			}
		}
		catch (final NumberFormatException ex)
		{
			throw new HeavenException("Le nombre {%1$s} est incorrect.", args[2]);
		}

		switch (args[0].toLowerCase())
		{
			case "ville":
				toTown = plugin.getTownProvider().getTownByName(args[1]);
				if (toTown != null)
				{
					toBankAccount = plugin.getBankAccountProvider().getBankAccountByTown(toTown);
				}
				else
				{
					throw new TownNotFoundException(args[1]);
				}
				break;

			case "joueur":
				final Optional<RPUser> optUser = plugin.getUserProvider().getOptionalUserByName(args[1]);

				if (!optUser.isPresent())
				{
					throw new UserNotFoundException(args[1]);
				}
				toUser = optUser.get();
				if (toUser.getName().equals(player.getName()))
				{
					throw new HeavenException("Alors comme ça, on s'envoie de l'argent à soi-même ?");
				}
				toBankAccount = plugin.getBankAccountProvider().getBankAccountByUser(toUser);
				break;

			case "entreprise":
				toCompany = plugin.getCompanyProvider().getCompanyByTag(args[1]);

				if (toCompany != null)
				{
					toBankAccount = plugin.getBankAccountProvider().getBankAccountByCompany(toCompany);
				}
				else
				{
					throw new CompanyNotFoundException(args[1]);
				}
				break;

			default:
				sendUsage(player);
				return;

		}
		try
		{
			new BankAccountMoneyTransfertQuery(fromBankAccount, toBankAccount, delta).executeQuery();
			ChatUtil.sendMessage(player, "Le virement a été effectué.");
		}
		catch (final SQLException e)
		{
			throw new HeavenException(e.getMessage());
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "Format de commande incorrect:");
		ChatUtil.sendMessage(sender, "/{payer} joueur <nom du joueur> <somme>");
		ChatUtil.sendMessage(sender, "/{payer} ville <nom de la ville> <somme>");
		ChatUtil.sendMessage(sender, "/{payer} entreprise <nom de l'entreprise> <somme>");
	}

}
