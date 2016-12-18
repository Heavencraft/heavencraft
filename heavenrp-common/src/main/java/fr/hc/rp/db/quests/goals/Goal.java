package fr.hc.rp.db.quests.goals;

public class Goal
{
	private final GoalAction action;
	private final int number;
	private final String element;

	Goal(GoalAction action, int number, String element)
	{
		this.action = action;
		this.number = number;
		this.element = element;
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
}