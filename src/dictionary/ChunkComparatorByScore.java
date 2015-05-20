package dictionary;

import java.util.Comparator;

public class ChunkComparatorByScore implements Comparator<Chunk> {

	@Override
	public int compare(Chunk c1, Chunk c2) {
			return (c1.getScore() < c2.getScore()) ? 1 : -1;
	}
}
