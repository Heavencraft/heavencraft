package fr.hc.rp.db.quests.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.tasks.queries.Query;

public class UpdateServerQuestStepSchematicQuery implements Query
{
	private static final String QUERY = "UPDATE server_quest_steps SET schematic = ? WHERE id = ? LIMIT 1;";

	private final ServerQuestStep step;
	private final byte[] schematic;
	private final ServerQuestStepProvider provider;

	public UpdateServerQuestStepSchematicQuery(ServerQuestStep step, byte[] schematic, ServerQuestStepProvider provider)
	{
		this.step = step;
		this.schematic = schematic;
		this.provider = provider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = provider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setBytes(1, schematic);
			ps.setInt(2, step.getId());

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new UnexpectedErrorException();

			provider.invalidateCache(step);
		}
	}
}