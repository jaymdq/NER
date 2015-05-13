package dictionary;

import java.util.Comparator;

public class ChunkComparator implements Comparator<Chunk>{

	@Override
	public int compare(Chunk c1, Chunk c2) {
		return (c1.start() > c2.start()) ? 1 : -1;
	}
	
}
