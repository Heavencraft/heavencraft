package fr.hc.rp.db.quests;

import java.util.Collection;

public interface QuestStep
{
	QuestStep getNextStep();

	Collection<Goal> getGoals();

	default boolean isFinalStep()
	{
		return getNextStep() == null;
	}
}