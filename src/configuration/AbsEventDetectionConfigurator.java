package configuration;

import java.util.Vector;

import utils.Pair;
import dictionary.chunk.Chunk;

public abstract class AbsEventDetectionConfigurator extends AbsConfigurator {

	// Variables
	protected Vector< Pair< String, Vector<Chunk> > > tweets;
	
	// Constructors
	
	public AbsEventDetectionConfigurator(String name, Vector< Pair< String, Vector<Chunk> > > tweets) {
		super(name);
		setTweets(tweets);
	}

	// Getters and Setters

	protected Vector<Pair<String, Vector<Chunk>>> getTweets() {
		return tweets;
	}

	protected void setTweets(Vector<Pair<String, Vector<Chunk>>> tweets) {
		this.tweets = tweets;
	}
	
	// Methods

}
