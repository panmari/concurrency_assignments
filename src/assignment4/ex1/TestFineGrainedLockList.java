package assignment4.ex1;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class TestFineGrainedLockList {

	private IFineGrainedLockList list;
	private Class<?> listClass;

	@Parameters
	public static Collection<?> getImplementations() {
		Object[][] a = new Object[][] { { FineGrainedLockList.class },
				{ OptimisticFineGrainedLockList.class } };
		return Arrays.asList(a);
	}

	public TestFineGrainedLockList(Class<?> cl) {
		this.listClass = cl;
	}

	@Before
	public void setUp() throws InstantiationException, IllegalAccessException {
		list = (IFineGrainedLockList) listClass.newInstance();
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
