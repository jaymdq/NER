package event;

import java.util.Vector;

import dictionary.chunk.Chunk;
import ner.NER;

public class Twevent {

	private NER ner;
	private Vector<String> tweets; //TODO cambiar a Status
	private FixedWindow fixedWindow;
	
	public Twevent(NER ner, Vector<String>  tweets){
		this.ner = ner;
		this.tweets = tweets;
		this.fixedWindow = new FixedWindow(10); //TODO
	}
	
	//TODO CAmbiar Nombre
	public void execute(boolean toLowerCase){
		
		//Esto  pasaria el ner por toda la lista de tweets
		
		for (String tweet : this.tweets){
			Vector<Chunk> chunksDetected = ner.recognize(tweet);
			this.fixedWindow.addTweet(tweet, chunksDetected);
		}
		
		//Dentro de los chunks encontrados se debe tratar de encontrar un evento
		System.out.println(this.fixedWindow.getSortedEvents());
		
	}
	
}
