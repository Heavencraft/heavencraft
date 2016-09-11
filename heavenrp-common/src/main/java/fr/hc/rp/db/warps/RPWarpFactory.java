package fr.hc.rp.db.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.db.warps.WarpFactory;

public class RPWarpFactory implements WarpFactory<RPWarp>
{

	@Override
	public RPWarp newWarp(ResultSet rs) throws SQLException
	{
		return new RPWarp(rs);
	}

}
