package fr.hc.rp.db.quests.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.QuestStep;
import fr.hc.rp.db.quests.goals.Goals;
import fr.hc.rp.db.quests.goals.GoalsParser;

public class ServerQuestStep implements QuestStep
{
	private final int id;
	private final int nextStep;
	private final byte[] schematic;

	private final Goals goals;

	// Available from package only
	ServerQuestStep(ResultSet rs) throws SQLException
	{
		id = rs.getInt("id");
		nextStep = rs.getInt("next_step");
		schematic = rs.getBytes("schematic");

		goals = GoalsParser.parseGoals(rs.getString("goals"));
	}

	public int getId()
	{
		return id;
	}

	@Override
	public Optional<ServerQuestStep> getNextStep() throws HeavenException
	{
		return HeavenRPInstance.get().getServerQuestStepProvider().getOptionalById(nextStep);
	}

	public byte[] getSchematic()
	{
		return schematic;
	}

	@Override
	public Goals getGoals()
	{
		return goals;
	}
}