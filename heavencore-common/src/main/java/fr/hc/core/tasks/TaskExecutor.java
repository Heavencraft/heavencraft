package fr.hc.core.tasks;

public interface TaskExecutor<T extends Task> extends Runnable
{
	void schedule(T task);
}