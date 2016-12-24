package fr.hc.rp.db.quests.goals;

import fr.hc.core.utils.LocalStringBuilder;

public class Goal
{
	private static final int HASHCODE_PRIME = 31;

	private final GoalAction action;
	private final int number;
	private final String element;

	Goal(GoalAction action, int number, String element)
	{
		this.action = action;
		this.number = number;
		this.element = element;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Goal))
			return false;

		final Goal other = (Goal) obj;

		return other.action == action && other.number == number && other.element.equals(element);
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = HASHCODE_PRIME * result + (action != null ? action.hashCode() : 0);
		result = HASHCODE_PRIME * result + number;
		result = HASHCODE_PRIME * result + (element != null ? element.hashCode() : 0);
		return result;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = LocalStringBuilder.get();
		builder.append(action);
		builder.append(' ');
		builder.append(number);
		builder.append(' ');
		builder.append(element);
		return LocalStringBuilder.release(builder);
	}

	public GoalAction getAction()
	{
		return action;
	}

	public int getNumber()
	{
		return number;
	}

	public String getElement()
	{
		return element;
	}

	public boolean isSimilar(Goal goal)
	{
		return action == goal.getAction() && element.equals(goal.getElement());
	}

	public Goal add(int toAdd)
	{
		return new Goal(action, number + toAdd, element);
	}

	public Goal substract(int toSubstract)
	{
		return new Goal(action, number - toSubstract, element);
	}

	public Goal createSimilar(int number)
	{
		return new Goal(action, number, element);
	}
}