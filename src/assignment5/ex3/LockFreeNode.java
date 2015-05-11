package assignment5.ex3;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeNode {

	int value;
	AtomicReference<LockFreeNode> next = new AtomicReference<LockFreeNode>();
	
	public LockFreeNode(int value) {
		this.value = value;
	}
}
