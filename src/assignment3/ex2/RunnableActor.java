package assignment3.ex2;

public abstract class RunnableActor implements Runnable {

	private final int maxActions;
	
	public RunnableActor() {
		this(100000);
	}
	public RunnableActor(int maxActions) {
		this.maxActions = maxActions;
	}
	
	@Override
	public void run() {
		for(int actions = 0; actions < maxActions; actions++ ) {
			act();
		}
	}

	
	abstract protected void act();
	
}
