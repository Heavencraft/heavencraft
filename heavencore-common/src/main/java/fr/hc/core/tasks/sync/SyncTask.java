package fr.hc.core.tasks.sync;

import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.tasks.Task;

public interface SyncTask extends Task
{
	@Override
	default void schedule()
	{
		HeavenCoreInstance.get().getTaskManager().schedule(this);
	}
}