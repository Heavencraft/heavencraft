package fr.hc.core;

import junit.framework.TestCase;

public class HeavenBlockLocationTest extends TestCase
{
	public void testEquals()
	{
		final HeavenBlockLocation location = new HeavenBlockLocation("world", 1, 2, 3);

		assertEquals(true, location.equals(new HeavenBlockLocation("world", 1, 2, 3)));
		assertEquals(false, location.equals(new HeavenBlockLocation("world_nether", 1, 2, 3)));
		assertEquals(false, location.equals(new HeavenBlockLocation("world", 10, 2, 3)));
		assertEquals(false, location.equals(new HeavenBlockLocation("world", 1, 20, 3)));
		assertEquals(false, location.equals(new HeavenBlockLocation("world", 1, 2, 30)));
	}

	public void testHashCore()
	{
		final int hashCode = new HeavenBlockLocation("world", 1, 2, 3).hashCode();

		assertEquals(hashCode, new HeavenBlockLocation("world", 1, 2, 3).hashCode());
		assertNotSame(hashCode, new HeavenBlockLocation("world_nether", 1, 2, 3).hashCode());
		assertNotSame(hashCode, new HeavenBlockLocation("world", 10, 2, 3).hashCode());
		assertNotSame(hashCode, new HeavenBlockLocation("world", 1, 20, 3).hashCode());
		assertNotSame(hashCode, new HeavenBlockLocation("world", 1, 2, 30).hashCode());
	}
}