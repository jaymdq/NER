package dictionary.chunk.comparator;

import java.util.Comparator;

import dictionary.chunk.Chunk;

public class ChunkComparatorByStart implements Comparator<Chunk>{

	// Variables
	
	// Constructors
	
	// Getters And Setters
	
	// Methods
	
	@Override
	public int compare(Chunk c1, Chunk c2) {
		Integer i1 = c1.start();
		Integer i2 = c2.start();
		return i1.compareTo(i2);
	}
	
}
