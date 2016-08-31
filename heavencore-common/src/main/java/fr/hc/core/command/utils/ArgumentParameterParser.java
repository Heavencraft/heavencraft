package fr.hc.core.command.utils;

import java.util.ArrayList;
import java.util.List;

import fr.hc.core.exceptions.HeavenException;

/**
 * This class offers methods to parse user input.
 * 
 * @author Manuel
 *
 */
public class ArgumentParameterParser
{
	protected static final String WRONG_ARGUMENT_FORMAT = "Un argument ne respecte pas le format 'arg:valeur': %1$s";
	protected static final String NUMERICAL_PARAMETER_OF_ARGUMENT_UNPARSABLE = "La valeur d'un argument n'est pas un nombre: %1$s";
	protected static final String UNKOWN_ARGUMENT = "Argument inconnu: %1$s";
	protected static final String FORBIDDEN_ARGUMENT_PASSED = "Un argument n'est pas autorisé dans ce contexte: %1$s";
	protected static final String OMMITED_MANDATORY_ARGUMENT = "Un argument obligatoire a été oublié: %1$s";
	protected static final String PASSED_TWO_TIMES_THE_SAME_ARGUMENT = "Un argument est présent plusieurs fois: %1$s";

	/**
	 * Parses an array of parameters to a Parameter Result object.
	 * 
	 * @param conf
	 * @param arguments
	 * @return
	 * @throws HeavenException
	 */
	public static ArgumentResult parse(CommandArgumentsConfiguration conf, String... arguments) throws HeavenException
	{
		ArgumentResult rsl = new ArgumentResult();
		for (int i = 0; i < arguments.length; i++)
		{
			// If the parameter is empty, pass on to the next.
			if (arguments[i].length() < 1)
				continue;
			UpdateArgumentResult(conf, rsl, arguments[i]);
		}

		if (CheckArgumentsFullfitted(conf, rsl))
			return rsl;
		else
			return null;
	}

	/**
	 * Checks if all mandatory arguments have been defined.
	 * 
	 * @param conf
	 * @param rsl
	 */
	private static boolean CheckArgumentsFullfitted(CommandArgumentsConfiguration conf, ArgumentResult rsl)
			throws HeavenException
	{
		if (conf.price == ArgumentRequirement.MANDATORY && !conf.treatedPrice)
			throw new HeavenException(OMMITED_MANDATORY_ARGUMENT, "price");

		if (conf.magicPoints == ArgumentRequirement.MANDATORY && !conf.treatedMagicPoints)
			throw new HeavenException(OMMITED_MANDATORY_ARGUMENT, "magicPoints");
		return true;
	}

	/**
	 * Checks if a parameter exists, and updates the value
	 * 
	 * @param rsl
	 * @param para
	 * @throws UnkownParameterPassed
	 */
	private static void UpdateArgumentResult(CommandArgumentsConfiguration conf, ArgumentResult rsl, String para)
			throws HeavenException
	{
		// Do we have "{type}:{data}"?
		String[] splitted = para.split(":");
		if (splitted.length != 2)
			throw new HeavenException(WRONG_ARGUMENT_FORMAT, para);
		// Does the prefix exist?
		String arg = splitted[0].toLowerCase();

		try
		{

			switch (arg)
			{

				case "price":
					if (conf.price == ArgumentRequirement.NO)
						throw new HeavenException(FORBIDDEN_ARGUMENT_PASSED, splitted[0]);
					if (conf.treatedPrice)
						throw new HeavenException(PASSED_TWO_TIMES_THE_SAME_ARGUMENT, splitted[0]);
					rsl.price = Integer.parseInt(splitted[1]);
					rsl.isPriceDefault = false;
					conf.treatedPrice = true;
					break;

				case "mpts":
				case "magicpoints":
					if (conf.magicPoints == ArgumentRequirement.NO)
						throw new HeavenException(FORBIDDEN_ARGUMENT_PASSED, splitted[0]);
					rsl.magicPoints = Integer.parseInt(splitted[1]);
					rsl.isMagicPointsDefault = false;
					conf.treatedMagicPoints = true;
					break;

				default:
					throw new HeavenException(UNKOWN_ARGUMENT, splitted[0]);
			}
		}
		catch (NumberFormatException e)
		{
			throw new HeavenException(NUMERICAL_PARAMETER_OF_ARGUMENT_UNPARSABLE, splitted[0]);
		}
	}

	/**
	 * Returns a formatted list of all allowed parameters for a given configuration.
	 * 
	 * @param conf
	 * @return
	 */
	private List<String> GetAllowedParameters(CommandArgumentsConfiguration conf)
	{
		final String OPT = " (optionnel)";
		final String MAND = " (obligatoire)";
		List<String> rsl = new ArrayList<String>();

		if (conf.price != ArgumentRequirement.NO)
		{
			String s = "'price' : Le prix en PO.";
			if (conf.price != ArgumentRequirement.MANDATORY)
				rsl.add(s + MAND);
			else
				rsl.add(s + OPT);
		}

		if (conf.magicPoints != ArgumentRequirement.NO)
		{
			String s = "'magicpoints', 'mpts' : Nombre de points de magie nécessaires.";
			if (conf.magicPoints != ArgumentRequirement.MANDATORY)
				rsl.add(s + MAND);
			else
				rsl.add(s + OPT);
		}
		return rsl;
	}

}
