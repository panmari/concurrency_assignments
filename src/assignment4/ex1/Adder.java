package assignment4.ex1;

import java.util.Iterator;
import java.util.List;

import assignment3.ex2.RunnableActor;

public class Adder extends RunnableActor {

	private IFineGrainedLockList queue;
	private Iterator<Integer> toAdd;
	
	public Adder(IFineGrainedLockList queue, List<Integer> toAdd) {
		super(toAdd.size());
		this.queue = queue;
		this.toAdd = toAdd.iterator();
	}
	
	@Override
	protected void act() {
		queue.add(toAdd.next());
	}
}