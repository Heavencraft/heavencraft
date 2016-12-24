package fr.hc.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import junit.framework.TestCase;

public class HeavenBlockLocationTest extends TestCase
{
	public void testEquals()
	{
		final HeavenBlockLocation location = new HeavenBlockLocation("world", 1, 2, 3);

		assertThat(location, equalTo(new HeavenBlockLocation("world", 1, 2, 3)));
		assertThat(location, not(equalTo(new HeavenBlockLocation(null, 1, 2, 3))));
		assertThat(location, not(equalTo(new HeavenBlockLocation("world_nether", 1, 2, 3))));
		assertThat(location, not(equalTo(new HeavenBlockLocation("world", 10, 2, 3))));
		assertThat(location, not(equalTo(new HeavenBlockLocation("world", 1, 20, 3))));
		assertThat(location, not(equalTo(new HeavenBlockLocation("world", 1, 2, 30))));
	}

	public void testHashCode()
	{
		final int hashCode = new HeavenBlockLocation("world", 1, 2, 3).hashCode();

		assertThat(hashCode, equalTo(new HeavenBlockLocation("world", 1, 2, 3).hashCode()));
		assertThat(hashCode, not(equalTo(new HeavenBlockLocation(null, 1, 2, 3).hashCode())));
		assertThat(hashCode, not(equalTo(new HeavenBlockLocation("world_nether", 1, 2, 3).hashCode())));
		assertThat(hashCode, not(equalTo(new HeavenBlockLocation("world", 10, 2, 3).hashCode())));
		assertThat(hashCode, not(equalTo(new HeavenBlockLocation("world", 1, 20, 3).hashCode())));
		assertThat(hashCode, not(equalTo(new HeavenBlockLocation("world", 1, 2, 30).hashCode())));
	}

	public void testToString()
	{
		assertThat(new HeavenBlockLocation("world", 1, 2, 3).toString(), equalTo("(world, 1, 2, 3)"));
	}
}