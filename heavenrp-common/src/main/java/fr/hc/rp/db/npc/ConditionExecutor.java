package fr.hc.rp.db.npc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.core.exceptions.HeavenException;
import fr.hc.core.utils.ConversionUtil;
import fr.hc.rp.HeavenRPInstance;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;

public class ConditionExecutor
{
	private static final Logger LOG = LoggerFactory.getLogger(ConditionExecutor.class);

	private static final Evaluator EVALUATOR = new Evaluator('\'', false, false, false, false);
	static
	{
		EVALUATOR.putFunction(new Function()
		{
			@Override
			public String getName()
			{
				return "getServerQuestCurrentStep";
			}

			@Override
			public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException
			{
				try
				{
					final int id = ConversionUtil.toUint(arguments);
					final int step = HeavenRPInstance.get().getServerQuestProvider().getById(id).getCurrentStep().getId();

					LOG.info("getServerQuestCurrentStep({}) => {}", arguments, step);
					return new FunctionResult(Integer.toString(step), FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
				}
				catch (final HeavenException ex)
				{
					throw new FunctionException(ex);
				}
			}
		});
	}

	public static boolean evaluate(String condition) throws HeavenException
	{
		try
		{
			// TODO: check, this is probably not working
			return EVALUATOR.evaluate(condition).equals("1.0");
		}
		catch (final EvaluationException ex)
		{
			LOG.error("Unable to evaluate expression {}", condition, ex);
			throw new HeavenException(ex.getMessage());
		}
	}
}