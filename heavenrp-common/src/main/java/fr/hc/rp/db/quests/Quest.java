package fr.hc.rp.db.quests;

public interface Quest
{
	QuestStep getInitialStep(); // Used to initialize QuestStatus
}