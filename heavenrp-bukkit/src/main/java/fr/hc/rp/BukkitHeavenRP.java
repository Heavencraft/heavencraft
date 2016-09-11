package fr.hc.rp;

import org.bukkit.Bukkit;

import fr.hc.core.AbstractBukkitPlugin;
import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.exceptions.StopServerException;
import fr.hc.core.users.UsersListener;
import fr.hc.guard.HeavenGuardInstance;
import fr.hc.rp.commands.BourseCommand;
import fr.hc.rp.commands.SpawnCommand;
import fr.hc.rp.db.bankaccounts.BankAccountProvider;
import fr.hc.rp.db.companies.CompanyProvider;
import fr.hc.rp.db.towns.TownProvider;
import fr.hc.rp.db.users.RPUserProvider;
import fr.hc.rp.db.warps.RPWarpProvider;
import fr.hc.rp.warps.WarpCommandExecutor;

public class BukkitHeavenRP extends AbstractBukkitPlugin implements HeavenRP
{
	private ConnectionProvider connectionProvider;

	private BankAccountProvider bankAccountProvider;
	private CompanyProvider companyProvider;
	private TownProvider townProvider;
	private RPUserProvider userProvider;
	private RPWarpProvider warpProvider;

	public BukkitHeavenRP()
	{
		HeavenRPInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			saveDefaultConfig();

			connectionProvider = createConnectionProvider(getConfig());
			initDatabaseIfNeeded(connectionProvider, "SELECT * FROM users LIMIT 1");

			bankAccountProvider = new BankAccountProvider();
			companyProvider = new CompanyProvider(connectionProvider);
			townProvider = new TownProvider(connectionProvider);
			userProvider = new RPUserProvider(connectionProvider);
			warpProvider = new RPWarpProvider(connectionProvider);

			HeavenGuardInstance.get().setUserProvider(userProvider);
			new UsersListener(userProvider);
			new WarpCommandExecutor(this);
			new SpawnCommand(this);
			new BourseCommand(this);
		}
		catch (final StopServerException ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	@Override
	public BankAccountProvider getBankAccountProvider()
	{
		return bankAccountProvider;
	}

	@Override
	public CompanyProvider getCompanyProvider()
	{
		return companyProvider;
	}

	@Override
	public TownProvider getTownProvider()
	{
		return townProvider;
	}

	@Override
	public RPUserProvider getUserProvider()
	{
		return userProvider;
	}

	public RPWarpProvider getWarpProvider()
	{
		return warpProvider;
	}
}