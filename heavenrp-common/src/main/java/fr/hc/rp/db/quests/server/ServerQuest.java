package fr.hc.rp.db.quests.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.Goal;
import fr.hc.rp.db.quests.QuestStatus;

public class ServerQuest implements QuestStatus
{
	private final int id;
	private final int currentStep;
	private final boolean completed;

	// Available from package only
	ServerQuest(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		currentStep = rs.getInt("current_step");
		completed = rs.getBoolean("completed");
	}

	public int getId()
	{
		return id;
	}

	@Override
	public ServerQuestStep getCurrentStep()
	{
		return HeavenRPInstance.get().getServerQuestStepProvider().getById(currentStep);
	}

	@Override
	public boolean hasBeenCompleted()
	{
		return completed;
	}

	@Override
	public Collection<Goal> getCompletedGoals()
	{
		// TODO Auto-generated method stub
		return null;
	}
}