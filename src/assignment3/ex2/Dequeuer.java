package assignment3.ex2;

import java.util.concurrent.CyclicBarrier;

public class Dequeuer extends RunnableActor {

	private IIntQueue queue;
	
	public Dequeuer(CyclicBarrier barrier, IIntQueue queue, int nrElements) {
		super(barrier, nrElements);
		this.queue = queue;
	}
	
	public Dequeuer(CyclicBarrier barrier, IIntQueue queue) {
		super(barrier);
		this.queue = queue;
	}
	
	@Override
	protected void act() {
		queue.deq();
	}

}
