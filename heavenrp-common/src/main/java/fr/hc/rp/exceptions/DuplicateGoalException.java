package fr.hc.rp.exceptions;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.quests.goals.GoalAction;

public class DuplicateGoalException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public DuplicateGoalException(GoalAction action, String element)
	{
		super("Duplicate goal for %1$s %2$s", action, element);
	}
}