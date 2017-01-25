package fr.hc.rp.db.quests.goals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;

public class GoalsParser
{
	private static final Logger log = LoggerFactory.getLogger(GoalsParser.class);

	// Format: "<Goal>,<Goal>,<Goal>"
	public static Goals parseGoals(String goalsString)
	{
		final Goals goals = new Goals();

		if (goalsString == null || (goalsString = goalsString.trim()).isEmpty())
			return goals;

		for (final String goalString : goalsString.split(","))
		{
			try
			{
				goals.add(parseGoal(goalString));
			}
			catch (final HeavenException ex)
			{
				log.error("Unable add goal: {}", goalString, ex);
			}
		}

		return goals;
	}

	// Format: "<Action> <number> <element>"
	private static Goal parseGoal(String goalString) throws HeavenException
	{
		final String[] goalStringSplitted = goalString.split(" ", 3);

		final GoalAction action = GoalAction.valueOf(goalStringSplitted[0]);
		final int number = ConversionUtil.toUint(goalStringSplitted[1]);
		final String element = goalStringSplitted[2];

		return new Goal(action, number, element);
	}
}