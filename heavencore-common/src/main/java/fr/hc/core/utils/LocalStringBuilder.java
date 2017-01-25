package fr.hc.core.utils;

import java.util.LinkedList;
import java.util.Queue;

public class LocalStringBuilder
{
	private static final ThreadLocal<Queue<StringBuilder>> localQueue = new ThreadLocal<Queue<StringBuilder>>()
	{
		@Override
		protected java.util.Queue<StringBuilder> initialValue()
		{
			return new LinkedList<StringBuilder>();
		}
	};

	public static StringBuilder get()
	{
		final Queue<StringBuilder> queue = localQueue.get();

		if (!queue.isEmpty())
		{
			final StringBuilder builder = queue.poll();
			builder.setLength(0);
			return builder;
		}

		return new StringBuilder();
	}

	public static String release(StringBuilder builder)
	{
		final String result = builder.toString();
		localQueue.get().add(builder);
		return result;
	}
}