package fr.hc.rp.quests.server;

import org.bukkit.inventory.Inventory;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.rp.HeavenRPInstance;
import fr.hc.rp.db.quests.server.ServerQuest;

public class ServerQuestInventoryContext
{
	private final Inventory inventory;
	private final int questId;

	public ServerQuestInventoryContext(Inventory inventory, int questId)
	{
		this.inventory = inventory;
		this.questId = questId;
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public ServerQuest getQuest() throws HeavenException
	{
		return HeavenRPInstance.get().getServerQuestProvider().getById(questId);
	}
}