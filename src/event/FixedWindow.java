package event;

import java.util.Collections;
import java.util.Vector;

import org.apache.commons.math3.analysis.function.Sigmoid;

import utils.Pair;
import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;


public class FixedWindow {

	// Variables

	private int fixedSize;
	private Vector<Pair<String, Vector<Chunk>>> tweets;
	private Vector<ChunkEvent> probBursty;

	// Constructors

	public FixedWindow(int fixedSize) {
		this.setFixedSize(fixedSize);
		this.tweets = new Vector<Pair<String,Vector<Chunk>>>();
		this.probBursty = new Vector<ChunkEvent>();
	}

	// Getters And Setters

	public int getFixedSize() {
		return fixedSize;
	}

	public void setFixedSize(int fixedSize) {
		this.fixedSize = fixedSize;
	}

	public void setTweets(Vector<Pair<String, Vector<Chunk>>> tweets) {
		this.tweets = tweets;
	}

	public Vector<Pair<String, Vector<Chunk>>> getTweets(){
		return this.tweets;
	}
	
	public int getNumberOfTweetsInWindow(){
		return this.tweets.size();
	}

	// Methods

	public void addTweet(Pair<String, Vector<Chunk>> tweet){
		if (this.tweets.size() == fixedSize){
			this.tweets.remove(0);
		}
		this.tweets.add(tweet);


		this.probBursty.clear();

		for (Chunk chunk : getAvailablesChunks()){
			Double probBurst = calculateProb(chunk);
			ChunkEvent chunkEvent = new ChunkEvent(chunk.getText(),chunk.getCategoryType(), probBurst);
			if (this.probBursty.contains(chunkEvent))
				this.probBursty.set(this.probBursty.indexOf(chunkEvent),chunkEvent);
			else
				this.probBursty.add(chunkEvent);
		}
	}

	private Double calculateProb(Chunk chunk){
		Double ps = 0.5; //TODO ver esto
		Double expectedLimit = (double) getNumberOfTweetsInWindow() * ps;
		Double standardDesviation = (double) getNumberOfTweetsInWindow() * ps * (1 - ps);
		Double fs = (double) getFrecuencyOfSegment(chunk.getText(), chunk.getCategoryType());

		if (fs > expectedLimit){
			// is considered as Bursty
			if (fs >= expectedLimit + 2.0 * Math.sqrt(standardDesviation)){
				return 1.0;
			}else{
				Sigmoid sigmoid = new Sigmoid();
				return sigmoid.value(10.0 * ((fs - (expectedLimit + standardDesviation)) / standardDesviation)); 
			}
		}

		return -1.0;
	}

	private int getFrecuencyOfSegment(String text, String category){
		int out = 0;
		Vector<Pair<String, Vector<Chunk>>> tempTweets = getTweetsWithSegment(text,category);

		for (Pair<String, Vector<Chunk>> pair1 : tempTweets){
			String tmp = pair1.getPair1().toLowerCase();
			int x = 0;
			while ( (x =  tmp.indexOf(text, x)) >= 0 ){
				out++;
				x += text.length();
			}
		}

		return out;
	}

	private Vector<Pair<String, Vector<Chunk>>> getTweetsWithSegment(String text, String category){
		Vector<Pair<String, Vector<Chunk>>> out = new Vector<Pair<String, Vector<Chunk>>>();

		for (Pair<String, Vector<Chunk>> tweet : this.tweets){
			if (tweet.getPair1().contains(text)){

				for (Chunk chunk : tweet.getPair2()){
					if (chunk.getCategoryType().equals(category) && ! out.contains(tweet)){
						out.add(tweet);

					}
				}
			}
		}
		return out;
	}



	private Vector<Chunk> getAvailablesChunks(){
		Vector<Chunk> out = new Vector<Chunk>();

		for (Pair<String, Vector<Chunk>> tweet : this.tweets){
			for (Chunk chunk : tweet.getPair2()){
				out.add(chunk);
			}
		}

		return out;
	}

	public Vector<ChunkEvent> getSortedEvents(){
		Collections.sort(this.probBursty);
		return this.probBursty;
	}
}
