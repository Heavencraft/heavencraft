package fr.hc.rp.db.warps;

import fr.hc.core.connection.ConnectionProvider;
import fr.hc.core.db.warps.WarpProvider;

public class RPWarpProvider extends WarpProvider<RPWarp>
{

	public RPWarpProvider(ConnectionProvider connectionProvider)
	{
		super(connectionProvider, new RPWarpFactory());
	}

}
