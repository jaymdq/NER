package event;

import java.util.Vector;

import utils.Pair;
import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;

public interface EventDetection {
	
	// Methods
	
	/**
	 * 
	 * @param debugMode El parametro debugMode denota si el modo debug se usara.
	 * @return El metodo detectEvents retorna un vector con eventos detectados.
	 */
	public abstract Vector<ChunkEvent> detectEvents(Vector< Pair< String, Vector<Chunk> > >  tweets,boolean debugMode);

	
}
