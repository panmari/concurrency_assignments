package assignment3.ex2;

public class Dequeuer extends RunnableActor {

	private IIntQueue queue;
	
	public Dequeuer(IIntQueue queue, int nrElements) {
		super(nrElements);
		this.queue = queue;
	}
	
	@Override
	protected void act() {
		queue.deq();
	}

}
