package assignment5.ex3;

import java.util.concurrent.atomic.AtomicReference;

import assignment3.ex2.IIntQueue;

public class UnboundLockFreeQueue implements IIntQueue {

	AtomicReference<LockFreeNode> head = new AtomicReference<LockFreeNode>();
	AtomicReference<LockFreeNode> tail = new AtomicReference<LockFreeNode>();

	public UnboundLockFreeQueue() {
		// create sentinel
		LockFreeNode sentinel = new LockFreeNode(Integer.MIN_VALUE);
		head.set(sentinel);
		tail.set(sentinel);
	}
	
	@Override
	public void enq(int value) {
		LockFreeNode newNode = new LockFreeNode(value);
		while (true) {
			LockFreeNode last = tail.get();
			LockFreeNode next = last.next.get();
			if (last == tail.get()) {
				if (next == null) {
					if (last.next.compareAndSet(next, newNode)) {
						tail.compareAndSet(last, newNode);
						return;
					}
				} else {
					tail.compareAndSet(last, next);
				}
			}
		}
	}

	@Override
	public int deq() {
		while (true) {
			LockFreeNode first = head.get();
			LockFreeNode last = tail.get();
			LockFreeNode next = first.next.get();
			if (first == head.get()) {
				if (first == last) {
					if (next == null) {
						throw new RuntimeException("Queue is empty.");
					}
					tail.compareAndSet(last, next);
				} else {
					int value = next.value;
					if (head.compareAndSet(first, next))
						return value;
				}
			}
		}
	}

}
