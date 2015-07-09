package event.twevent;

import java.util.Vector;

import org.apache.log4j.Logger;
import utils.Pair;
import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;
import event.EventDetection;

public class Twevent implements EventDetection{

	// Variables

	private FixedWindow fixedWindow;
	private double lowerLimit;
	private double observationProb;
	
	// Constructors
	
	public Twevent( FixedWindow fixedWindow, double lowerLimit, double observationProb){
		this.setFixedWindow(fixedWindow);
		this.setLowerLimit(lowerLimit);
		this.setObservationProb(observationProb);
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
	
	public double getObservationProb() {
		return observationProb;
	}

	public void setObservationProb(double observationProb) {
		this.observationProb = observationProb;
	}
	
	// Methods

	public Vector<ChunkEvent> detectEvents(Vector< Pair< String, Vector<Chunk> > >  tweets, boolean debugMode){

		Vector<ChunkEvent> out = new Vector<ChunkEvent>();
		
		fixedWindow.clear();
		fixedWindow.setExpectedObervationProb(getObservationProb());
		
		for (Pair< String, Vector<Chunk> > tweet : tweets){
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