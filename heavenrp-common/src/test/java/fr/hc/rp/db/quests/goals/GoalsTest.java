package fr.hc.rp.db.quests.goals;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import fr.hc.rp.exceptions.DuplicateGoalException;
import junit.framework.TestCase;

public class GoalsTest extends TestCase
{
	public void testToString() throws DuplicateGoalException
	{
		final Goals goals = new Goals();
		goals.add(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE"));
		goals.add(new Goal(GoalAction.GIVE_ITEM, 12, "LOG"));

		assertThat(goals.toString(), equalTo("GIVE_ITEM 24 COBBLESTONE,GIVE_ITEM 12 LOG"));
	}

	public void testEquals() throws DuplicateGoalException
	{
		final Goals goals1 = new Goals();
		goals1.add(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE"));
		goals1.add(new Goal(GoalAction.GIVE_ITEM, 12, "LOG"));

		final Goals goals2 = new Goals();
		goals2.add(new Goal(GoalAction.GIVE_ITEM, 12, "LOG"));
		goals2.add(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE"));

		assertThat(goals1, equalTo(goals2));

		goals2.add(new Goal(GoalAction.KILL, 24, "COBBLESTONE"));
		assertThat(goals1, not(equalTo(goals2)));

		goals1.add(new Goal(GoalAction.KILL, 24, "COBBLESTONE"));
		assertThat(goals1, equalTo(goals2));
	}

	public void testHashCode() throws DuplicateGoalException
	{
		final Goals goals1 = new Goals();
		goals1.add(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE"));
		goals1.add(new Goal(GoalAction.GIVE_ITEM, 12, "LOG"));

		final Goals goals2 = new Goals();
		goals2.add(new Goal(GoalAction.GIVE_ITEM, 12, "LOG"));
		goals2.add(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE"));

		assertThat(goals1.hashCode(), equalTo(goals2.hashCode()));

		goals2.add(new Goal(GoalAction.KILL, 24, "COBBLESTONE"));
		assertThat(goals1.hashCode(), not(equalTo(goals2.hashCode())));

		goals1.add(new Goal(GoalAction.KILL, 24, "COBBLESTONE"));
		assertThat(goals1.hashCode(), equalTo(goals2.hashCode()));
	}
}