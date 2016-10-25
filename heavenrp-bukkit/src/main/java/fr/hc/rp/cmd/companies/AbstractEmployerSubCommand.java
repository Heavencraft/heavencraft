package fr.hc.rp.cmd.companies;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.core.cmd.SubCommand;
import fr.hc.core.db.users.User;
import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRP;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.companies.Company;

public abstract class AbstractEmployerSubCommand extends SubCommand
{
	protected final HeavenRP plugin = HeavenRPInstance.get();

	public AbstractEmployerSubCommand(String permission)
	{
		super(permission);
	}

	@Override
	public boolean canSeeUsage(CommandSender sender)
	{
		return true;
	}

	@Override
	public boolean canExecute(CommandSender sender, String[] args)
	{
		if (super.canExecute(sender, args))
			return true;

		if (args == null || args.length == 0)
			return false;

		return canExecute(sender, args[0]);
	}

	public boolean canExecute(CommandSender sender, String companyTag)
	{
		if (!(sender instanceof Player))
			return false;

		final UUID uuid = ((Player) sender).getUniqueId();

		try
		{
			final Optional<? extends User> optUser = plugin.getUserProvider().getOptionalUserByUniqueId(uuid);
			if (!optUser.isPresent())
				return false;

			final Company company = plugin.getCompanyProvider().getCompanyByTag(companyTag);
			return company.isEmployer(optUser.get());
		}
		catch (final HeavenException e)
		{
			return false;
		}
	}
}