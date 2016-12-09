package fr.hc.rp.db.npc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NPCMessage
{
	private final int id;
	private final int npcId;
	private final String message;

	// Available from package only
	NPCMessage(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		npcId = rs.getInt("npc_id");
		message = rs.getString("message");
	}

	public int getId()
	{
		return id;
	}

	public int getNpcId()
	{
		return npcId;
	}

	public String getMessage()
	{
		return message;
	}
}