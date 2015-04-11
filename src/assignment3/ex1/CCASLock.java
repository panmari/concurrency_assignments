package assignment3.ex1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CCASLock implements Lock {

	private AtomicInteger a = new AtomicInteger(0);
	
	@Override
	public void lock() {
		while(true) {
			// Wait while lock is in use
			while(a.get() == 1) {}
			// If it was seen as unlocked, try to lock,
			// break loop and return if successfully set.
			if (a.compareAndSet(0, 1))
				return;
		}
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
	public void unlock() {
		a.set(0);
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
