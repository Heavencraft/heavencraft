package fr.hc.core.command.utils;

/**
 * This class is used to filter allowed arguments for a command.
 * 
 * @author Manuel
 *
 */
public class CommandArgumentsConfiguration
{
	public ArgumentRequirement price = ArgumentRequirement.NO;
	protected boolean treatedPrice = false;
	public ArgumentRequirement magicPoints = ArgumentRequirement.NO;
	protected boolean treatedMagicPoints = false;

	public CommandArgumentsConfiguration(ArgumentRequirement price, ArgumentRequirement magicPoints)
	{
		this.price = price;
		this.magicPoints = magicPoints;
	}
}
