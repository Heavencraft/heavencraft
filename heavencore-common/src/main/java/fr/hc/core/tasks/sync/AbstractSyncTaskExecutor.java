package fr.hc.core.tasks.sync;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.TaskExecutor;

public abstract class AbstractSyncTaskExecutor implements TaskExecutor<SyncTask>
{
	private final Queue<SyncTask> tasks = new ConcurrentLinkedQueue<SyncTask>();

	@Override
	public void schedule(SyncTask task)
	{
		tasks.add(task);
	}

	@Override
	public void run()
	{
		SyncTask task;

		while ((task = tasks.poll()) != null)
		{
			try
			{
				task.execute();
				task.onSuccess();
			}
			catch (final HeavenException ex)
			{
				task.onException(ex);
			}
		}
	}
}