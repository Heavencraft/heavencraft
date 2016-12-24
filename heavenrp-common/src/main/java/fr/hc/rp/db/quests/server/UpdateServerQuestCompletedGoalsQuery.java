package fr.hc.rp.db.quests.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.tasks.queries.Query;
import fr.hc.rp.db.quests.goals.Goal;
import fr.hc.rp.db.quests.goals.Goals;

public class UpdateServerQuestCompletedGoalsQuery implements Query
{
	private static final String QUERY = "UPDATE server_quests SET completed_goals = ? WHERE id = ? AND completed = false LIMIT 1;";

	private final ServerQuest quest;
	private final Collection<Goal> goals;
	private final ServerQuestProvider provider;

	public UpdateServerQuestCompletedGoalsQuery(ServerQuest quest, Goals goals, ServerQuestProvider provider)
	{
		this(quest, goals.getAll(), provider);
	}

	public UpdateServerQuestCompletedGoalsQuery(ServerQuest quest, Collection<Goal> goals, ServerQuestProvider provider)
	{
		this.quest = quest;
		this.goals = goals;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, Goals.toString(goals));
			ps.setInt(2, quest.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new UnexpectedErrorException();

			provider.invalidateCache(quest);
		}
	}
}