package fr.hc.rp.db.npc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NPCMessage
{
	private final int id;
	private final String npcTag;
	private final String message;

	// Available from package only
	NPCMessage(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		npcTag = rs.getString("npc_tag");
		message = rs.getString("message");
	}

	public int getId()
	{
		return id;
	}

	public String getNpcTag()
	{
		return npcTag;
	}

	public String getMessage()
	{
		return message;
	}
}