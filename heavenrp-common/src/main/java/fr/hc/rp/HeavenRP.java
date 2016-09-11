package fr.hc.rp;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.rp.db.bankaccounts.BankAccountProvider;
import fr.hc.rp.db.companies.CompanyProvider;
import fr.hc.rp.db.towns.TownProvider;
import fr.hc.rp.db.users.RPUserProvider;
import fr.hc.rp.db.warps.RPWarpProvider;

public interface HeavenRP
{
	ConnectionProvider getConnectionProvider();

	BankAccountProvider getBankAccountProvider();

	CompanyProvider getCompanyProvider();

	TownProvider getTownProvider();

	RPUserProvider getUserProvider();
	
	RPWarpProvider getWarpProvider();
	
}