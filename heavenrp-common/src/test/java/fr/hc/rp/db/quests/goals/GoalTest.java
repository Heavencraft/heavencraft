package fr.hc.rp.db.quests.goals;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import junit.framework.TestCase;

public class GoalTest extends TestCase
{
	public void testEquals()
	{
		final Goal goal = new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE");

		assertThat(goal, equalTo(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE")));
		assertThat(goal, not(equalTo(new Goal(GoalAction.KILL, 24, "COBBLESTONE"))));
		assertThat(goal, not(equalTo(new Goal(GoalAction.GIVE_ITEM, 23, "COBBLESTONE"))));
		assertThat(goal, not(equalTo(new Goal(GoalAction.GIVE_ITEM, 24, "STONE"))));
	}

	public void testHashCode()
	{
		final int hashCode = new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE").hashCode();

		assertThat(hashCode, equalTo(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE").hashCode()));
		assertThat(hashCode, not(equalTo(new Goal(GoalAction.KILL, 24, "COBBLESTONE").hashCode())));
		assertThat(hashCode, not(equalTo(new Goal(GoalAction.GIVE_ITEM, 23, "COBBLESTONE").hashCode())));
		assertThat(hashCode, not(equalTo(new Goal(GoalAction.GIVE_ITEM, 24, "STONE").hashCode())));
	}

	public void testToString()
	{
		assertThat(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE").toString(), equalTo("GIVE_ITEM 24 COBBLESTONE"));
	}

	public void testIsSimilar()
	{
		final Goal goal = new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE");

		assertTrue(goal.isSimilar(new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE")));
		assertTrue(goal.isSimilar(new Goal(GoalAction.GIVE_ITEM, 32, "COBBLESTONE")));
		assertFalse(goal.isSimilar(new Goal(GoalAction.KILL, 24, "COBBLESTONE")));
		assertFalse(goal.isSimilar(new Goal(GoalAction.GIVE_ITEM, 24, "STONE")));
	}

	public void testAdd()
	{
		final Goal goal = new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE");

		assertThat(goal.add(0), equalTo(goal));
		assertThat(goal.add(8), equalTo(new Goal(GoalAction.GIVE_ITEM, 32, "COBBLESTONE")));
		assertThat(goal.add(-8), equalTo(new Goal(GoalAction.GIVE_ITEM, 16, "COBBLESTONE")));
	}

	public void testSubstract()
	{
		final Goal goal = new Goal(GoalAction.GIVE_ITEM, 24, "COBBLESTONE");

		assertThat(goal.substract(0), equalTo(goal));
		assertThat(goal.substract(8), equalTo(new Goal(GoalAction.GIVE_ITEM, 16, "COBBLESTONE")));
		assertThat(goal.substract(-8), equalTo(new Goal(GoalAction.GIVE_ITEM, 32, "COBBLESTONE")));
	}
}