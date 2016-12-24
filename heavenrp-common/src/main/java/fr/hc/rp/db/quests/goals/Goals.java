package fr.hc.rp.db.quests.goals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.exceptions.UnexpectedErrorException;
import fr.hc.core.utils.LocalStringBuilder;
import fr.hc.rp.exceptions.DuplicateGoalException;

public class Goals
{
	private final Map<GoalAction, Map<String, Goal>> goalsByElementByAction = new HashMap<GoalAction, Map<String, Goal>>();
	private final Collection<Goal> goals = new ArrayList<Goal>();

	public Goals()
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Goals))
			return false;

		final Goals other = (Goals) obj;

		return goalsByElementByAction.equals(other.goalsByElementByAction);
	}

	@Override
	public int hashCode()
	{
		return goalsByElementByAction.hashCode();
	}

	@Override
	public String toString()
	{
		return toString(goals);
	}

	public static String toString(Collection<Goal> goals)
	{
		final StringBuilder builder = LocalStringBuilder.get();
		final Iterator<Goal> it = goals.iterator();

		while (it.hasNext())
		{
			builder.append(it.next().toString());
			if (it.hasNext())
				builder.append(',');
		}

		return LocalStringBuilder.release(builder);
	}

	public void add(Goal goal) throws DuplicateGoalException
	{
		Map<String, Goal> goalsByElement = goalsByElementByAction.get(goal.getAction());

		if (goalsByElement == null)
			goalsByElementByAction.put(goal.getAction(), goalsByElement = new HashMap<String, Goal>());

		if (goalsByElement.putIfAbsent(goal.getElement(), goal) != null)
			throw new DuplicateGoalException(goal.getAction(), goal.getElement());

		goals.add(goal);
	}

	public Goal get(GoalAction action, String element)
	{
		final Map<String, Goal> goalsByElement = goalsByElementByAction.get(action);
		if (goalsByElement == null)
			return null;
		return goalsByElement.get(element);
	}

	public Collection<Goal> getAll()
	{
		return goals;
	}

	public Goals substract(Goals toSubstruct) throws HeavenException
	{
		try
		{
			final Goals result = new Goals();

			for (final Entry<GoalAction, Map<String, Goal>> actionEntry : goalsByElementByAction.entrySet())
			{
				final GoalAction action = actionEntry.getKey();

				for (final Entry<String, Goal> elementEntry : actionEntry.getValue().entrySet())
				{
					final String element = elementEntry.getKey();

					final Goal toSubstractGoal = toSubstruct.get(action, element);
					if (toSubstractGoal != null)
						result.add(elementEntry.getValue().substract(toSubstractGoal.getNumber()));
					else
						result.add(elementEntry.getValue());
				}
			}

			return result;
		}
		catch (final DuplicateGoalException ex)
		{
			ex.printStackTrace();
			throw new UnexpectedErrorException();
		}
	}
}