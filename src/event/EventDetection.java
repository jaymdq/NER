package event;

import java.util.Vector;

import utils.Pair;
import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;

public abstract class EventDetection {
	
	// Variables
	protected Vector< Pair< String, Vector<Chunk> > > tweets;
	
	// Constructors
	
	protected EventDetection(Vector< Pair< String, Vector<Chunk> > > tweets){
		setTweets(tweets);
	}
	
	// Getters And Setters
	
	public Vector<Pair<String, Vector<Chunk>>> getTweets() {
		return tweets;
	}

	public void setTweets(Vector<Pair<String, Vector<Chunk>>> tweets) {
		this.tweets = tweets;
	}
	
	// Methods
	
	/**
	 * 
	 * @param debugMode El parametro debugMode denota si el modo debug se usara.
	 * @param toLowerCase El parametro toLowerCase especifica si se debe tratar el texto en LowerCase.
	 * @return El metodo detectEvents retorna un vector con eventos detectados.
	 */
	public abstract Vector<ChunkEvent> detectEvents(boolean debugMode, boolean toLowerCase);

	
}
