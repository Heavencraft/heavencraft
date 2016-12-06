package fr.hc.rp.db.quests;

public interface QuestStep
{
	QuestStep getNextStep();

	default boolean isFinalStep()
	{
		return getNextStep() == null;
	}
}