package fr.hc.rp.db.quests;

import java.util.Optional;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.db.quests.goals.Goals;

public interface QuestStep
{
	Optional<? extends QuestStep> getNextStep() throws HeavenException;

	Goals getGoals();

	default boolean isFinalStep() throws HeavenException
	{
		return getNextStep() == null;
	}
}