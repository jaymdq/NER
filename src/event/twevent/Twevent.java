package event.twevent;

import java.util.Vector;

import twitter4j.Logger;
import utils.Pair;
import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;
import event.EventDetection;

public class Twevent extends EventDetection{

	// Variables

	private FixedWindow fixedWindow;
	private double lowerLimit;

	// Constructors
	
	public Twevent(Vector< Pair< String, Vector<Chunk> > > tweets){
		super(tweets);
		this.setFixedWindow(new FixedWindow(tweets.size()));
		this.setLowerLimit(0.0);
	}

	public Twevent(Vector< Pair< String, Vector<Chunk> > >  tweets, FixedWindow fixedWindow, double loweLimit){
		super(tweets);
		this.setFixedWindow(fixedWindow);
		this.setLowerLimit(loweLimit);
	}

	// Getters And Setters

	public FixedWindow getFixedWindow() {
		return fixedWindow;
	}

	public void setFixedWindow(FixedWindow fixedWindow) {
		this.fixedWindow = fixedWindow;
	}

	public double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	
	// Methods

	//TODO Ver si es necesario pasarle el toLowerCase
	public Vector<ChunkEvent> detectEvents(boolean debugMode, boolean toLowerCase){

		Vector<ChunkEvent> out = new Vector<ChunkEvent>();
		
		for (Pair< String, Vector<Chunk> > tweet : this.tweets){
			this.fixedWindow.addTweet(tweet);
		}

		//Dentro de los chunks encontrados se debe tratar de encontrar un evento
		Vector<ChunkEvent> sortedEvents = this.fixedWindow.getSortedEvents();
		
		for (ChunkEvent event : sortedEvents){
			if (event.getScore() >= this.lowerLimit){
				out.add(event);
			}
		}
		
		if (debugMode){
			Logger.getLogger(Twevent.class).info("Sorted Events : " + sortedEvents);
			Logger.getLogger(Twevent.class).info("Filtered Events: " + out);
		}
		
		return out;
	}

}
/*
public class Twevent {

	// Variables

	private NER ner;
	private Vector<String> tweets; //TODO cambiar a Status
	private FixedWindow fixedWindow;
	private double lowerLimit;

	// Constructors
	
	public Twevent(NER ner, Vector<String>  tweets){
		this.setNer(ner);
		this.setTweets(tweets);
		this.setFixedWindow(new FixedWindow(tweets.size()));
		this.setLowerLimit(0.0);
	}

	public Twevent(NER ner, Vector<String>  tweets, FixedWindow fixedWindow, double loweLimit){
		this.setNer(ner);
		this.setTweets(tweets);
		this.setFixedWindow(fixedWindow);
		this.setLowerLimit(loweLimit);
	}

	// Getters And Setters

	public NER getNer() {
		return ner;
	}

	public void setNer(NER ner) {
		this.ner = ner;
	}

	public Vector<String> getTweets() {
		return tweets;
	}

	public void setTweets(Vector<String> tweets) {
		this.tweets = tweets;
	}

	public FixedWindow getFixedWindow() {
		return fixedWindow;
	}

	public void setFixedWindow(FixedWindow fixedWindow) {
		this.fixedWindow = fixedWindow;
	}

	public double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	
	// Methods

	//TODO Ver si es necesario pasarle el toLowerCase
	public Vector<ChunkEvent> detectEvents(boolean debugMode, boolean toLowerCase){

		Vector<ChunkEvent> out = new Vector<ChunkEvent>();
		
		for (String tweet : this.tweets){
			Vector<Chunk> chunksDetected = ner.recognize(tweet);
			this.fixedWindow.addTweet(tweet, chunksDetected);
		}

		//Dentro de los chunks encontrados se debe tratar de encontrar un evento
		Vector<ChunkEvent> sortedEvents = this.fixedWindow.getSortedEvents();
		
		for (ChunkEvent event : sortedEvents){
			if (event.getScore() >= this.lowerLimit){
				out.add(event);
			}
		}
		
		if (debugMode){
			Logger.getLogger(Twevent.class).info("Sorted Events : " + sortedEvents);
			Logger.getLogger(Twevent.class).info("Filtered Events: " + out);
		}
		
		return out;
	}

}
*/