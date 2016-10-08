package fr.hc.rp.cmd.companies;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UserNotFoundException;
import fr.hc.core.utils.chat.ChatUtil;
import fr.hc.rp.RPPermissions;
import fr.hc.rp.db.companies.AddCompanyMemberQuery;
import fr.hc.rp.db.companies.Company;
import fr.hc.rp.db.users.RPUser;

public class EntrepriseAjouterEmployeSubCommand extends AbstractEmployerSubCommand
{
	public EntrepriseAjouterEmployeSubCommand()
	{
		super(RPPermissions.ENTREPRISE_AJOUTEREMPLOYE_COMMAND);
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	public void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(sender);
			return;
		}

		final String companyTag = args[0];
		final String userName = args[1];

		final Optional<RPUser> optUser = plugin.getUserProvider().getUserByName(userName);
		if (optUser.isPresent())
			throw new UserNotFoundException(userName);
		final User user = optUser.get();

		final Company company = plugin.getCompanyProvider().getCompanyByTag(companyTag);

		new AddCompanyMemberQuery(company, user, true, plugin.getCompanyProvider())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(sender, "Le joueur {%1$s} est désormais employé dans l'entreprise {%2$s}.", user,
						company);
			}

			@Override
			public void onException(HeavenException ex)
			{
				ChatUtil.sendMessage(sender, ex.getMessage());
			}
		}.schedule();

	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{entreprise} ajouterEmployé <tag de l'entreprise> <nom du joueur>");
	}
}