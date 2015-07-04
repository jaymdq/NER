package examples.arff.filter;

import java.util.Vector;

import dictionary.chunk.Chunk;

public abstract class ParamFilterAbs {

	protected String[] values;
	
	public abstract String apply(Vector<Chunk> chunks);
	
}
