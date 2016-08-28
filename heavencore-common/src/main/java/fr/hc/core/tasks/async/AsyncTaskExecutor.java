package fr.hc.core.tasks.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.tasks.TaskExecutor;

public class AsyncTaskExecutor implements TaskExecutor<AsyncTask>
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final BlockingQueue<AsyncTask> tasks = new LinkedBlockingQueue<AsyncTask>();

	public AsyncTaskExecutor()
	{
		new Thread(this, "AsyncTaskExecutor").start();
	}

	@Override
	public void schedule(AsyncTask task)
	{
		tasks.add(task);
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				final AsyncTask task = tasks.take(); // Blocks until a task is available

				try
				{
					task.execute();
					new SuccessCallbackTask(task).schedule();
				}
				catch (final HeavenException ex)
				{
					new ExceptionCallbackTask(task, ex).schedule();
				}
			}
		}
		catch (final InterruptedException ex)
		{
			log.error("AsyncTaskExecutor has been interrupted", ex);
		}
	}
}