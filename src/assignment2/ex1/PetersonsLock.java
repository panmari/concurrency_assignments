package assignment2.ex1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PetersonsLock implements Lock {
	volatile AtomicIntegerArray level;
	volatile AtomicIntegerArray victim;
	private int nThreads;

	public PetersonsLock(int nThreads) {
		// These are implicitly initialized to 0.
		level = new AtomicIntegerArray(nThreads);
		victim = new AtomicIntegerArray(nThreads);
		this.nThreads = nThreads;
	}

	@Override
	public void lock() {
		long threadId = Thread.currentThread().getId();
		int id = threadIdToId(threadId);
		for (int L = 1; L < nThreads; L++) {
			level.set(id, L);
			victim.set(L, id);
			while (canNotAdvance(id, L)) {
			}
		}
	}

	@Override
	public void unlock() {
		long threadId = Thread.currentThread().getId();
		int id = threadIdToId(threadId);
		level.set(id, 0);
	}

	private int threadIdToId(long threadId) {
		return (int) threadId % nThreads;
	}

	/**
	 * Returns false if this thread can advance.
	 * 
	 * @param id
	 * @param currentLevel
	 * @return
	 */
	private boolean canNotAdvance(int id, int currentLevel) {
		boolean threadWithHigherLevelExists = false;
		for (int k = 0; k < nThreads; k++) {
			if (k == id)
				continue;
			// Return immediately if there is another thread with higher
			// level/priority.
			if (level.get(k) >= currentLevel) {
				threadWithHigherLevelExists = true;
				break;
			}
		}
		return threadWithHigherLevelExists && victim.get(currentLevel) == id;
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
}
