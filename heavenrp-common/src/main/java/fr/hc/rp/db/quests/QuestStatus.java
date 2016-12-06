package fr.hc.rp.db.quests;

public interface QuestStatus
{
	QuestStep getCurrentStep();

	boolean hasBeenCompleted();
}