package fr.hc.core.utils;

public class LocalStringBuilder
{
	private static final ThreadLocal<StringBuilder> localStringBuilder = new ThreadLocal<StringBuilder>()
	{
		@Override
		protected StringBuilder initialValue()
		{
			return new StringBuilder();
		}

		@Override
		public StringBuilder get()
		{
			final StringBuilder builder = super.get();
			builder.setLength(0);
			return builder;
		}
	};

	public static StringBuilder get()
	{
		return localStringBuilder.get();
	}
}