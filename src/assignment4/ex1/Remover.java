package assignment4.ex1;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import assignment3.ex2.RunnableActor;

public class Remover extends RunnableActor {

	private IFineGrainedLockList queue;
	private Iterator<Integer> toRemove;
	
	public Remover(CyclicBarrier barrier, IFineGrainedLockList queue, List<Integer> toRemove) {
		super(barrier, toRemove.size());
		this.queue = queue;
		this.toRemove = toRemove.iterator();
	}
	
	@Override
	protected void act() {
		queue.remove(toRemove.next());
	}

}
