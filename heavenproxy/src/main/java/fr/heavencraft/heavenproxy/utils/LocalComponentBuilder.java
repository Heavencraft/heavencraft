package fr.heavencraft.heavenproxy.utils;

import net.md_5.bungee.api.chat.ComponentBuilder;

public class LocalComponentBuilder
{
	private static final ThreadLocal<ComponentBuilder> localComponentBuilder = new ThreadLocal<ComponentBuilder>()
	{
		@Override
		protected ComponentBuilder initialValue()
		{
			return new ComponentBuilder("");
		}

		@Override
		public ComponentBuilder get()
		{
			final ComponentBuilder builder = super.get();
			builder.reset();
			return builder;
		}
	};

	public static ComponentBuilder get()
	{
		return localComponentBuilder.get();
	}
}