package fr.heavencraft.heavenproxy.exceptions;

public class HeavenException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public HeavenException(String message)
	{
		super(message);
	}
	
	public HeavenException(String message, Object... args)
	{
		super(String.format(message, args));
	}
}