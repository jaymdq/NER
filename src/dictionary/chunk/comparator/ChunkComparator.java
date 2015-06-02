package dictionary.chunk.comparator;

import java.util.Comparator;

import dictionary.chunk.Chunk;

public class ChunkComparator implements Comparator<Chunk>{

	@Override
	public int compare(Chunk c1, Chunk c2) {
		return (c1.start() > c2.start()) ? 1 : -1;
	}
	
}
