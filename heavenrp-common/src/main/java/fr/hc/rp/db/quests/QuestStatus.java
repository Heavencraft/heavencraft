package fr.hc.rp.db.quests;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.quests.goals.Goals;

public interface QuestStatus
{
	QuestStep getCurrentStep() throws HeavenException;

	Goals getCompletedGoals();

	boolean hasBeenCompleted();
}