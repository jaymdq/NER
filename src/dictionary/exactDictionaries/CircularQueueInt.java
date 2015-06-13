package dictionary.exactDictionaries;

import java.util.Arrays;

public class CircularQueueInt {

	// Variables
	private int[] queue;
	private int nextPos = 0;

	// Constructors
	public CircularQueueInt(int size) {
		queue = new int[size];
		Arrays.fill(queue,0);
	}

	// Getters And Setters
	
	// Methods
	
	public void enqueue(int val) {
		queue[nextPos] = val;
		if (++nextPos == queue.length)
			nextPos = 0;
	}
	public int get(int offset) {
		int pos = nextPos - offset;
		if (pos < 0)
			pos += queue.length;
		return queue[pos];
	}

}
