package ner;

import org.apache.log4j.Logger;

import dictionary.ExactDictionary;

public class NER {

	/*TODO
	 * La idea seria meter aca toda la funcionalidad de la entrada, procesamiento , cambio de estrategias y la salida
	 */

	private boolean debugMode;

	public NER(){
		setDebugMode(false);
	}

	public NER(boolean debugMode){
		setDebugMode(debugMode);
	}

	public boolean isDebugModeActivated(){
		return this.debugMode;
	}

	public void setDebugMode(boolean debugMode){
		this.debugMode = debugMode;
		if (debugMode)
			Logger.getLogger(NER.class).debug("DEBUG MODE IS ACTIVATED");
	}

	//TODO puede ser que haya un diccionario abstracto.. o hacer varios recognize
	public void recognize(ExactDictionary dictionary, String text){
		
		if (debugMode){
			System.out.println(dictionary.toString() + "\n");
		}


		System.out.println(dictionary.recognize(text,debugMode));

	}
}
