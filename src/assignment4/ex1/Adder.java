package assignment4.ex1;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import assignment3.ex2.RunnableActor;

public class Adder extends RunnableActor {

	private IFineGrainedLockList queue;
	private Iterator<Integer> toAdd;
	
	public Adder(CyclicBarrier barrier, IFineGrainedLockList queue, List<Integer> toAdd) {
		super(barrier, toAdd.size());
		this.queue = queue;
		this.toAdd = toAdd.iterator();
	}
	
	@Override
	protected void act() {
		queue.add(toAdd.next());
	}
}