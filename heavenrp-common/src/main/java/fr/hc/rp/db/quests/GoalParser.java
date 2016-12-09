package fr.hc.rp.db.quests;

import java.util.ArrayList;
import java.util.Collection;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.rp.db.quests.Goal.GoalType;

public class GoalParser
{
	// Format: "<Goal>,<Goal>,<Goal>"
	public static Collection<Goal> parseGoals(String goalsString) throws HeavenException
	{
		final Collection<Goal> goals = new ArrayList<Goal>();

		for (final String goalString : goalsString.split(","))
		{
			goals.add(parseGoal(goalString));
		}

		return goals;
	}

	// Format: "<Action> <number> <element>"
	private static Goal parseGoal(String goalString) throws HeavenException
	{
		final String[] goalStringSplitted = goalString.split(" ", 3);

		final GoalType action = GoalType.valueOf(goalStringSplitted[0]);
		final int number = ConversionUtil.toUint(goalStringSplitted[1]);
		final String element = goalStringSplitted[2];

		return new Goal(action, number, element);
	}
}