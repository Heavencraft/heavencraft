package fr.hc.core.tasks.async;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.sync.SyncTask;

// Callback task used by AsyncTaskExecutor to call AsyncTask.onException(HeavenException)
public class ExceptionCallbackTask implements SyncTask
{
	private final AsyncTask task;
	private final HeavenException ex;

	public ExceptionCallbackTask(AsyncTask task, HeavenException ex)
	{
		this.task = task;
		this.ex = ex;
	}

	@Override
	public void execute()
	{
		task.onException(ex);
	}
}