package fr.hc.core.tasks.async;

import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.tasks.Task;

public interface AsyncTask extends Task
{
	@Override
	default void schedule()
	{
		HeavenCoreInstance.get().getTaskManager().schedule(this);
	}
}