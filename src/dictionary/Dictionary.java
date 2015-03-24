package dictionary;

import java.util.Vector;

public interface Dictionary {

	public Vector<Chunk> recognize(String text, boolean debugMode);
		
}
