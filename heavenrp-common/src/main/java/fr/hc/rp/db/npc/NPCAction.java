package fr.hc.rp.db.npc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NPCAction
{
	private final int id;
	private final String npcTag;
	private final String conditions;
	private final String[] messages;
	private final String[] commands;

	// Available from package only
	NPCAction(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		npcTag = rs.getString("npc_tag");
		conditions = rs.getString("conditions");

		final String messagesString = rs.getString("messages");
		messages = rs.wasNull() ? new String[0] : messagesString.split(";");

		final String commandsString = rs.getString("commands");
		commands = rs.wasNull() ? new String[0] : commandsString.split(";");
	}

	public int getId()
	{
		return id;
	}

	public String getNpcTag()
	{
		return npcTag;
	}

	public boolean hasConditions()
	{
		return conditions != null && !conditions.isEmpty();
	}

	public String getConditions()
	{
		return conditions;
	}

	public boolean hasMessages()
	{
		return messages.length != 0;
	}

	public String[] getMessages()
	{
		return messages;
	}

	public boolean hasCommands()
	{
		return commands.length != 0;
	}

	public String[] getCommands()
	{
		return commands;
	}
}