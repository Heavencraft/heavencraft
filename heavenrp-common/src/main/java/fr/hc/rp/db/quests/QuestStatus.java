package fr.hc.rp.db.quests;

import java.util.Collection;

public interface QuestStatus
{
	QuestStep getCurrentStep();

	Collection<Goal> getCompletedGoals();

	boolean hasBeenCompleted();
}