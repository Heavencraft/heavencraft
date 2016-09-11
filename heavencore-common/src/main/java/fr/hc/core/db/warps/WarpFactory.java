package fr.hc.core.db.warps;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface WarpFactory<W extends Warp>
{
	public W newWarp(ResultSet rs) throws SQLException;
}
