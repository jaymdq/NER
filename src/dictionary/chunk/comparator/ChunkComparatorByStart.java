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
		return (c1.start() > c2.start()) ? 1 : -1;
	}
	
}
