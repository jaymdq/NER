package ner;

import java.util.Vector;

import org.apache.log4j.Logger;

import dictionary.Chunk;
import dictionary.Dictionary;

public class NER {

	/*TODO
	 * La idea seria meter aca toda la funcionalidad de la entrada, procesamiento , cambio de estrategias y la salida
	 */
	
	private Vector<Dictionary> dictionaries;
	
	private boolean debugMode;

	public NER(){
		setDebugMode(false);
		dictionaries = new Vector<Dictionary>();
	}

	public NER(boolean debugMode){
		setDebugMode(debugMode);
		dictionaries = new Vector<Dictionary>();
	}
	
	public NER(boolean debugMode,Vector<Dictionary> dictionaries){
		setDebugMode(debugMode);
		setDictionaries(dictionaries);
	}

	public boolean isDebugModeActivated(){
		return this.debugMode;
	}

	public void setDebugMode(boolean debugMode){
		this.debugMode = debugMode;
		if (debugMode)
			Logger.getLogger(NER.class).debug("DEBUG MODE IS ACTIVATED");
	}

	
	public Vector<Chunk> recognize(String text){
		Vector<Chunk> out = new Vector<Chunk>();
		
		for (Dictionary dictionary : dictionaries) 
			out.addAll(dictionary.recognize(text,debugMode));
				
		if (debugMode)
			System.out.println(out);
		
		return out;

	}

	public Vector<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(Vector<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
	
	public void addDictionary(Dictionary dictionary){
		dictionaries.add(dictionary);
	}
}
