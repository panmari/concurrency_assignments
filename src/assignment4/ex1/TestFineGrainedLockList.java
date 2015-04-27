package assignment4.ex1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestFineGrainedLockList {

	
	private FineGrainedLockList list;

	@Before
	public void setUp() {
		this.list = new FineGrainedLockList();
	}
	
	@Test
	public void emptyListShouldNotContainNumbers() {
		assertFalse(list.contains(1));
		assertFalse(list.contains(-1));
		assertFalse(list.contains(10));
	}

	@Test
	public void emptyListShouldContainNumberAfterAdding() {

		assertFalse(list.contains(1));
		assertFalse(list.contains(-1));
		assertFalse(list.contains(10));
	}

	@Test
	public void listShouldReturnFalseIfAddingContainedElement() {
		assertTrue(list.add(1));
		assertTrue(list.add(2));
		assertTrue(list.add(10));
		assertFalse(list.add(1));
		assertFalse(list.add(10));
	}

	@Test
	public void listShouldSupportRemove() {
		assertTrue(list.add(1));
		assertTrue(list.contains(1));
		assertTrue(list.remove(1));
		assertFalse(list.contains(1));
	}
	
	@Test
	public void listShouldReturnFalseUponRemovingInexistingKey() {
		assertFalse(list.remove(1));
	}

}
