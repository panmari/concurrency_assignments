package assignment2.ex1;

public class Incrementor implements Runnable {

	private Counter counter;
	private int[] incrementorCount;
	private static final int MAX_COUNT = 300000;

	public Incrementor(Counter counter, int[] incrementorCount) {
		this.counter = counter;
		this.incrementorCount = incrementorCount;
	}

	@Override
	public void run() {
		while (true) {
			long threadId = Thread.currentThread().getId();
			counter.lock.lock();
			if (counter.getCount() < MAX_COUNT) {
				counter.increment();
				counter.lock.unlock();
				incrementorCount[(int) (threadId % incrementorCount.length)]++;
			} else {
				counter.lock.unlock();
				break;
			}
		}
	}
}