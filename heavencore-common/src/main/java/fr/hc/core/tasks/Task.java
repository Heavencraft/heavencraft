package fr.hc.core.tasks;

import fr.hc.core.exceptions.HeavenException;

public interface Task
{
	void schedule();

	void execute() throws HeavenException;

	default void onSuccess()
	{
	}

	default void onException(HeavenException ex)
	{
	}
}