package assignment5.ex3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import assignment3.ex2.IIntQueue;

@RunWith(value = Parameterized.class)
public class TestQueues {

	private IIntQueue queue;
	private Class<?> queueClass;

	@Parameters
	public static Collection<?> getImplementations() {
		Object[][] a = new Object[][] { { UnboundLockQueue.class },
				{ UnboundLockFreeQueue.class } };
		return Arrays.asList(a);
	}

	public TestQueues(Class<?> cl) {
		this.queueClass = cl;
	}

	@Before
	public void setUp() throws InstantiationException, IllegalAccessException {
		queue = (IIntQueue) queueClass.newInstance();
	}

	@Test(expected=RuntimeException.class)
	public void emptyQueueShouldThrowException() {
		queue.deq();
	}

	@Test
	public void oneItemShouldBeQueuedAndReturned() {
		queue.enq(1);
		assertEquals(1, queue.deq());
	}

	@Test
	public void twoItemsShouldBeQueuedInRightOrder() {
		queue.enq(1);
		queue.enq(2);
		assertEquals(2, queue.deq());
		assertEquals(1, queue.deq());
	}

	@Test
	public void afterEmptyingQueueShouldStillWork() {
		queue.enq(1);
		assertEquals(1, queue.deq());
		// Should be empty now, try some more stuff:
		queue.enq(1);
		queue.enq(2);
		assertEquals(2, queue.deq());
		assertEquals(1, queue.deq());
	}
	
	@Test
	public void shouldBeUnboundedAndHandleAzillionItems() {
		int aZillion = 10000;
		for (int i = 0; i < aZillion; i++)
			queue.enq(1);
		for (int i = 0; i < aZillion; i++)
			assertEquals(1, queue.deq());
	}
}
