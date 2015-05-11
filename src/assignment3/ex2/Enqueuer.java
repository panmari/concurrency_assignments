package assignment3.ex2;

import java.util.concurrent.CyclicBarrier;

public class Enqueuer extends RunnableActor {

	private IIntQueue queue;
	
	public Enqueuer(CyclicBarrier barrier, IIntQueue queue, int nrElements) {
		super(barrier, nrElements);
		this.queue = queue;
	}
	public Enqueuer(CyclicBarrier barrier, IIntQueue queue) {
		super(barrier);
		this.queue = queue;
	}
	
	@Override
	protected void act() {
		queue.enq(1);
	}

}
