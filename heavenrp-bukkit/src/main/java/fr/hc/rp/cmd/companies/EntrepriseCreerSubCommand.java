package fr.hc.rp.cmd.companies;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.bankaccounts.BankAccountMoneyTransfertQuery;
import fr.hc.rp.db.companies.AddCompanyMemberQuery;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.users.RPUser;

public class EntrepriseCreerSubCommand extends SubCommand
{
	private final HeavenRP plugin = HeavenRPInstance.get();

	private final Map<Integer, CompanyRequest> companyRequestByUserId = new HashMap<Integer, CompanyRequest>();

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length < 2)
		{
			sendUsage(player);
			return;
		}

		final RPUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		final int companyCreationCost = plugin.getPricingManager().getCompanyCreationCost();
		if (user.getBalance() < companyCreationCost)
			throw new HeavenException("Vous fouillez dans votre bourse... Vous n'avez pas assez.");

		final String name = ConversionUtil.toString(args, 1, " ");
		final String tag = args[0];

		final Optional<Company> optCompany = plugin.getCompanyProvider().getOptionalCompanyByTag(tag);
		if (optCompany.isPresent())
			throw new HeavenException("L'entreprise {%1$s} existe déjà.", tag);

		CompanyRequest companyRequest;

		if ((companyRequest = companyRequestByUserId.remove(user.getId())) == null
				|| !companyRequest.isSameRequest(name, tag))
		{
			companyRequestByUserId.put(user.getId(), new CompanyRequest(name, tag));
			ChatUtil.sendMessage(player, "La creation de l'entreprise vous coûtera {%1$s} pièces d'or.",
					companyCreationCost);
			ChatUtil.sendMessage(player, "Tapez la commande une seconde fois pour confirmer.");
			return;
		}

		new BankAccountMoneyTransfertQuery(user, plugin.getPricingManager().getHeavencraftAccount(),
				companyCreationCost)
		{
			@Override
			public void onSuccess()
			{
				try
				{
					final Company company = plugin.getCompanyProvider().createCompany(name, tag);

					new AddCompanyMemberQuery(company, user, true, plugin.getCompanyProvider())
					{
						@Override
						public void onSuccess()
						{
							ChatUtil.sendMessage(player, "L'entreprise {%1$s} a été crée avec succès.", company);
						}

						@Override
						public void onException(HeavenException ex)
						{
							ChatUtil.sendMessage(player, ex.getMessage());
						}
					}.schedule();
				}
				catch (final HeavenException ex)
				{
					ex.printStackTrace();
					ChatUtil.sendMessage(player, UnexpectedErrorException.MESSAGE);
				}
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}.schedule();
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		notConsoleCommand(sender);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{entreprise} creer <tag de l'entreprise> <nom de l'entreprise>");
	}

	class CompanyRequest
	{
		private final String name;
		private final String tag;

		public CompanyRequest(String name, String tag)
		{
			this.name = name;
			this.tag = tag;
		}

		public boolean isSameRequest(String name, String tag)
		{
			return this.name.equals(name) && this.tag.equals(tag);
		}
	}
}