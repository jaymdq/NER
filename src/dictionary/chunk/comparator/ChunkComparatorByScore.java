package dictionary.chunk.comparator;

import java.util.Comparator;

import dictionary.chunk.Chunk;

public class ChunkComparatorByScore implements Comparator<Chunk> {

	// Variables

	// Constructors

	// Getters And Setters

	// Methods

	@Override
	public int compare(Chunk c1, Chunk c2) {
		return (c1.getScore() < c2.getScore()) ? 1 : -1;
	}
}
