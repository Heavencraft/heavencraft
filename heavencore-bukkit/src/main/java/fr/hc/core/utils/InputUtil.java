package fr.hc.core.utils;

import java.util.Collection;
import java.util.Iterator;

import fr.hc.core.HeavenCoreInstance;
import fr.hc.core.db.users.User;
import fr.hc.core.db.users.UserProvider;
import fr.hc.core.exceptions.HeavenException;

public class InputUtil
{
	private static final ThreadLocal<StringBuilder> localBuilder = new ThreadLocal<StringBuilder>()
	{
		@Override
		protected StringBuilder initialValue()
		{
			return new StringBuilder();
		}
	};

	public static String collectionToString(Collection<Object> list, String separator)
	{
		final StringBuilder builder = localBuilder.get();

		try
		{

			final Iterator<Object> it = list.iterator();
			while (it.hasNext())
			{
				builder.append(it.next().toString());
				if (it.hasNext())
					builder.append(separator);
			}
			return builder.toString();
		}
		finally
		{
			builder.setLength(0);
		}
	}

	public static String userIdsToString(Collection<Integer> userIds, String separator) throws HeavenException
	{
		final StringBuilder builder = localBuilder.get();
		final UserProvider<? extends User> provider = HeavenCoreInstance.get().getUserProvider();

		try
		{

			final Iterator<Integer> it = userIds.iterator();
			while (it.hasNext())
			{
				builder.append(provider.getUserById(it.next()));
				if (it.hasNext())
					builder.append(separator);
			}
			return builder.toString();
		}
		finally
		{
			builder.setLength(0);
		}
	}

}