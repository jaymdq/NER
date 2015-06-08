package event;

import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.distribution.NormalDistribution;

import dictionary.chunk.Chunk;
import utils.Pair;

/*	
 * public static void main(String[] args) {
	// Problem 1; µ = 1000; σ = 100
			NormalDistribution d = new NormalDistribution(1000, 100);
	        System.out.println(d.cumulativeProbability(1200));
	        // Problem 2; µ = 50; σ = 10
	        d = new NormalDistribution(50, 10);
	        System.out.println(d.inverseCumulativeProbability(0.9));
}
 */

public class FixedWindow {

	private int fixedSize;
	private Vector<Pair<String, Vector<Chunk>>> tweets;
	private HashMap<Pair<String,String>,Double> probBursty;

	public FixedWindow(int fixedSize) {
		this.setFixedSize(fixedSize);
		this.tweets = new Vector<Pair<String,Vector<Chunk>>>();
		this.probBursty = new HashMap<Pair<String,String>,Double>();
	}

	public int getFixedSize() {
		return fixedSize;
	}

	public void setFixedSize(int fixedSize) {
		this.fixedSize = fixedSize;
	}

	public void addTweet(String text, Vector<Chunk> chunks){
		if (this.tweets.size() == fixedSize){
			this.tweets.remove(0);
		}
		this.tweets.add(new Pair<String, Vector<Chunk>>( text, chunks));


		//	for (Chunk chunk : chunks){
		for (Chunk chunk : getAvailablesChunks()){
			Double probBurst = calculateProb(chunk);
			this.probBursty.put(new Pair<String,String>(chunk.getText(),chunk.type()),probBurst);
		}
	}

	private Double calculateProb(Chunk chunk){
		Double ps = 0.5; //TODO ver esto
		Double expectedLimit = (double) getNumberOfTweetsInWindow() * ps;
		Double standardDesviation = (double) getNumberOfTweetsInWindow() * ps * (1 - ps);
		Double fs = (double) getFrecuencyOfSegment(chunk.getText(), chunk.type());

		if (fs > expectedLimit){
			//Seria considerado Bursty
			if (fs >= expectedLimit + 2 * Math.sqrt(standardDesviation)){
				return 1.0;
			}else{
				Sigmoid sigmoid = new Sigmoid();
				return sigmoid.value(10 * ((fs - (expectedLimit + standardDesviation)) / standardDesviation)); 
			}
		}

		return -1.0;
	}

	public int getNumberOfTweetsInWindow(){
		return this.tweets.size();
	}

	public Vector<Pair<String, Vector<Chunk>>> getTweets(){
		return this.tweets;
	}

	private Vector<Pair<String, Vector<Chunk>>> getTweetsWithSegment(String text, String category){
		Vector<Pair<String, Vector<Chunk>>> out = new Vector<Pair<String, Vector<Chunk>>>();

		for (Pair<String, Vector<Chunk>> tweet : this.tweets){
			if (tweet.getPair1().contains(text)){
				for (Chunk chunk : tweet.getPair2()){
					if (chunk.type().equals(category)){
						out.add(tweet);
					}
				}
			}
		}

		return out;
	}

	private int getFrecuencyOfSegment(String text, String category){
		return getTweetsWithSegment(text,category).size();
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

	
	public Vector<Pair<Pair<String,String>,Double>> getSortedEvents(){
		Vector<Pair<Pair<String,String>,Double>> possibleEvents = new Vector<Pair<Pair<String,String>,Double>>();
		for (Pair<String,String> key : this.probBursty.keySet()){
			possibleEvents.add(new Pair<Pair<String,String>,Double>(key,this.probBursty.get(key)));
		}
		
		
		
		
		return possibleEvents;
	}
}
