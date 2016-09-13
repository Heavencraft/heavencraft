package fr.hc.rp.db.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.db.warps.Warp;

public class RPWarp extends Warp
{
	protected RPWarp(ResultSet rs) throws SQLException
	{
		super(rs);
	}
}
