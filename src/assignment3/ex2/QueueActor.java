package assignment3.ex2;

public abstract class QueueActor implements Runnable {

	private final static int MAX_ACTIONS = 100000;
	
	
	@Override
	public void run() {
		for(int actions = 0; actions < MAX_ACTIONS; actions++ ) {
			act();
		}
	}

	
	abstract protected void act();
	
}
