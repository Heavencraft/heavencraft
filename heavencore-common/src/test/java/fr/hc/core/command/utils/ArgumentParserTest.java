package fr.hc.core.command.utils;

import static org.junit.Assert.*;

import fr.hc.core.command.utils.ArgumentParameterParser;
import fr.hc.core.command.utils.ArgumentRequirement;
import fr.hc.core.command.utils.ArgumentResult;
import fr.hc.core.command.utils.CommandArgumentsConfiguration;
import fr.hc.core.exceptions.HeavenException;

/**
 * Testing case for command parser
 * 
 * @author Manuel
 *
 */
public class ArgumentParserTest
{
	private final CommandArgumentsConfiguration confNothing = new CommandArgumentsConfiguration(ArgumentRequirement.NO,
			ArgumentRequirement.NO);
	private final CommandArgumentsConfiguration confOnlyPriceMandatory = new CommandArgumentsConfiguration(
			ArgumentRequirement.MANDATORY, ArgumentRequirement.NO);
	private final CommandArgumentsConfiguration confOnlyPriceOptional = new CommandArgumentsConfiguration(
			ArgumentRequirement.OPTIONAL, ArgumentRequirement.NO);

	@org.junit.Test
	public void ParserDoesNotChangeBaseValuesWhenNoParameterIsPassed()
	{
		try
		{
			ArgumentResult prs = ArgumentParameterParser.parse(confNothing, "");

			assertEquals("No parameter given, but value parsed has changed: price.", 0, prs.price);
			assertEquals("No parameter given, but value parsed has changed: Magic Points.", 0, prs.magicPoints);
		}
		catch (HeavenException e)
		{
			e.printStackTrace();
			fail("Error thrown when parsing empty string.");
		}
	}

	@org.junit.Test
	public void PriceParameterMandatoryIsParsed()
	{
		try
		{
			ArgumentResult prs = ArgumentParameterParser.parse(confOnlyPriceMandatory, "price:45");
			assertEquals("Price parameter is not parsed correctly.", 45, prs.price);
			assertFalse("Price has changed, so set the flag.", prs.isPriceDefault);
			assertEquals("Magic Points should not have changed.", 0, prs.magicPoints);
			assertTrue("Magic Points should not have changed.", prs.isMagicPointsDefault);
		}
		catch (HeavenException e)
		{
			fail("Error thrown when parsing empty string.");
		}
	}

	@org.junit.Test
	public void ParameterMandatorynessTest()
	{
		try
		{
			ArgumentParameterParser.parse(confOnlyPriceMandatory, "");
			fail("Have been able to parse something where a mandatory command has been omitted.");
		}
		catch (HeavenException e)
		{
			String expected = String.format(ArgumentParameterParser.OMMITED_MANDATORY_ARGUMENT, "price");
			assertEquals("Wrong error caught.", expected, e.getMessage());
		}
	}

	@org.junit.Test
	public void ParameterForbiddenTest()
	{
		try
		{
			ArgumentParameterParser.parse(confOnlyPriceMandatory, "magicpoints:6");
			fail("Have been able to parse a forbidden argument.");
		}
		catch (HeavenException e)
		{
			String expected = String.format(ArgumentParameterParser.FORBIDDEN_ARGUMENT_PASSED, "magicpoints");
			assertEquals("Wrong error caught.", expected, e.getMessage());
		}
	}

	@org.junit.Test
	public void ParameterOptionalynessTest()
	{
		try
		{
			ArgumentParameterParser.parse(confOnlyPriceOptional, "");
		}
		catch (HeavenException e)
		{
			fail("Failed to parse command where optional parameter has not been given..");
		}

		try
		{
			ArgumentResult prs = ArgumentParameterParser.parse(confOnlyPriceOptional, "price:46");
			assertFalse("Price was mandatory, and should have been changed since we passed an argument.",
					prs.isPriceDefault);
			assertEquals("Price was mandatory, and should have been changed since we passed an argument", 46,
					prs.price);
		}
		catch (HeavenException e)
		{
			fail("We recieved an error when parsing a mandatory argument.");
		}
	}

	@org.junit.Test
	public void ArgumentTwoTimesPassedTest()
	{
		try
		{
			ArgumentParameterParser.parse(confOnlyPriceMandatory, "price:42", "price:43");
			fail("Have been able set two times the same paramter.");
		}
		catch (HeavenException e)
		{
			String expected = String.format(ArgumentParameterParser.PASSED_TWO_TIMES_THE_SAME_ARGUMENT, "price");
			assertEquals("Wrong error caught.", expected, e.getMessage());
		}
	}

	@org.junit.Test
	public void UnexistingArgumentPassedTest()
	{
		try
		{
			ArgumentParameterParser.parse(confOnlyPriceMandatory, "price:66", "um4dBr0:42");
			fail("Have been able parse with an inexiting parameter.");
		}
		catch (HeavenException e)
		{
			String expected = String.format(ArgumentParameterParser.UNKOWN_ARGUMENT, "um4dBr0");
			assertEquals("Wrong error caught.", expected, e.getMessage());
		}
	}

	@org.junit.Test
	public void NotNumericalValuePassedForNumericalArgumentPassedTest()
	{
		// Number error
		try
		{
			ArgumentParameterParser.parse(confOnlyPriceMandatory, "price:unebaguette");
			fail("Have been able parse a non-numerical parameter into a number.");
		}
		catch (HeavenException e)
		{
			String expected = String.format(ArgumentParameterParser.NUMERICAL_PARAMETER_OF_ARGUMENT_UNPARSABLE,
					"price");
			assertEquals("Wrong error caught.", expected, e.getMessage());
		}
	}

}
