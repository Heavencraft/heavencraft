package fr.hc.rp.db.quests.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.QuestStep;

public class ServerQuestStep implements QuestStep
{
	private final int id;
	private final int nextStep;

	// Available from package only
	ServerQuestStep(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		nextStep = rs.getInt("next_step");
	}

	public int getId()
	{
		return id;
	}

	@Override
	public ServerQuestStep getNextStep()
	{
		return HeavenRPInstance.get().getServerQuestStepProvider().getById(nextStep);
	}
}