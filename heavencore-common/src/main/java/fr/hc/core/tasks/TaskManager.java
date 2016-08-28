package fr.hc.core.tasks;

import fr.hc.core.tasks.async.AsyncTask;
import fr.hc.core.tasks.sync.SyncTask;

public class TaskManager
{
	private final TaskExecutor<SyncTask> syncTaskExecutor;
	private final TaskExecutor<AsyncTask> asyncTaskExecutor;

	public TaskManager(TaskExecutor<SyncTask> syncTaskExecutor, TaskExecutor<AsyncTask> asyncTaskExecutor)
	{
		this.syncTaskExecutor = syncTaskExecutor;
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

	public void schedule(SyncTask task)
	{
		syncTaskExecutor.schedule(task);
	}

	public void schedule(AsyncTask task)
	{
		asyncTaskExecutor.schedule(task);
	}
}