package fr.hc.rp.db.quests.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.QuestStatus;
import fr.hc.rp.db.quests.goals.Goals;
import fr.hc.rp.db.quests.goals.GoalsParser;

public class ServerQuest implements QuestStatus
{
	private final int id;
	private final int currentStep;
	private final boolean completed;
	private final Goals completedGoals;

	// Available from package only
	ServerQuest(ResultSet rs) throws SQLException, HeavenException
	{
		id = rs.getInt("id");
		currentStep = rs.getInt("current_step");
		completed = rs.getBoolean("completed");

		completedGoals = GoalsParser.parseGoals(rs.getString("completed_goals"));
	}

	public int getId()
	{
		return id;
	}

	@Override
	public ServerQuestStep getCurrentStep() throws HeavenException
	{
		return HeavenRPInstance.get().getServerQuestStepProvider().getById(currentStep);
	}

	@Override
	public boolean hasBeenCompleted()
	{
		return completed;
	}

	@Override
	public Goals getCompletedGoals()
	{
		return completedGoals;
	}
}