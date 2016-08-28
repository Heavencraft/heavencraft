package fr.hc.core.tasks.async;

import fr.hc.core.tasks.sync.SyncTask;

// Callback task used by AsyncTaskExecutor to call AsyncTask.onSuccess()
public class SuccessCallbackTask implements SyncTask
{
	private final AsyncTask task;

	public SuccessCallbackTask(AsyncTask task)
	{
		this.task = task;
	}

	@Override
	public void execute()
	{
		task.onSuccess();
	}
}