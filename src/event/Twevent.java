package event;

import java.util.Vector;

import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;
import ner.NER;

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
		System.out.println("Sorted Events : " + sortedEvents);
		System.out.println("Filtered: " + out);
		return out;
	}

}
