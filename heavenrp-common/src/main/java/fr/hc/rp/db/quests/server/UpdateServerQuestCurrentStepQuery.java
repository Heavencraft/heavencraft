package fr.hc.rp.db.quests.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.tasks.queries.Query;

public class UpdateServerQuestCurrentStepQuery implements Query
{
	private static final String QUERY = "UPDATE server_quests SET current_step = ?, completed = ?, completed_goals = NULL WHERE id = ? AND completed = false LIMIT 1;";

	private final ServerQuest quest;
	private final ServerQuestStep currentStep;
	private final ServerQuestProvider provider;

	public UpdateServerQuestCurrentStepQuery(ServerQuest quest, ServerQuestStep currentStep, ServerQuestProvider provider)
	{
		this.quest = quest;
		this.currentStep = currentStep;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (quest.getCurrentStep().getId() == currentStep.getId())
			return; // Nothing to do

		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, currentStep.getId());
			ps.setBoolean(2, currentStep.isFinalStep());
			ps.setInt(3, quest.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new UnexpectedErrorException();

			provider.invalidateCache(quest);
		}
	}
}