package assignment3.ex2;

public class Enqueuer extends RunnableActor {

	private IIntQueue queue;
	
	public Enqueuer(IIntQueue queue, int nrElements) {
		super(nrElements);
		this.queue = queue;
	}
	public Enqueuer(IIntQueue queue) {
		this.queue = queue;
	}
	
	@Override
	protected void act() {
		queue.enq(1);
	}

}
