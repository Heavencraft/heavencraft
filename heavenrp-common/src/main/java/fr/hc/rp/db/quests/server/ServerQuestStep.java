package fr.hc.rp.db.quests.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.Goal;
import fr.hc.rp.db.quests.GoalParser;
import fr.hc.rp.db.quests.QuestStep;

public class ServerQuestStep implements QuestStep
{
	private final int id;
	private final int nextStep;
	private final byte[] schematic;

	private final Collection<Goal> goals;

	// Available from package only
	ServerQuestStep(ResultSet rs) throws SQLException, HeavenException
	{
		id = rs.getInt("id");
		nextStep = rs.getInt("next_step");
		schematic = rs.getBytes("schematic");

		goals = GoalParser.parseGoals(rs.getString("goals"));
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

	public byte[] getSchematic()
	{
		return schematic;
	}

	@Override
	public Collection<Goal> getGoals()
	{
		return goals;
	}
}