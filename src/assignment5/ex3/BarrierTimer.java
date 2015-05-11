package assignment5.ex3;

public class BarrierTimer implements Runnable {

	long start;
	long end;
	
	@Override
	public void run() {
		this.start = System.nanoTime();
	}

	
	public void stop() {
		end = System.nanoTime();
	}
	
	public double durationInMs() {
		return (end - start) / 1e6;
	}
}
