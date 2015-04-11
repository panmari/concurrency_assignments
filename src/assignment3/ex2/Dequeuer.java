package assignment3.ex2;

public class Dequeuer extends QueueActor {

	private IIntQueue queue;
	
	public Dequeuer(IIntQueue queue) {
		this.queue = queue;
	}
	
	@Override
	protected void act() {
		queue.deq();
	}

}
