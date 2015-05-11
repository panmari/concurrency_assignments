package assignment3.ex2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public abstract class RunnableActor implements Runnable {

	private final int maxActions;
	private CyclicBarrier barrier;

	public RunnableActor(CyclicBarrier barrier) {
		this(barrier, 100000);
	}
	public RunnableActor(CyclicBarrier barrier, int maxActions) {
		this.barrier = barrier;
		this.maxActions = maxActions;
	}
	
	@Override
	public void run() {
		try {
			barrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		for(int actions = 0; actions < maxActions; actions++ ) {
			act();
		}
	}

	
	abstract protected void act();
	
}
