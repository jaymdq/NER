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
	
	/**
	 * Constructor vacio, no se habilita el modo debug.
	 */
	
	public NER(){
		setDebugMode(false);
		dictionaries = new Vector<Dictionary>();
	}
	
	/**
	 * 
	 * @param debugMode El valor true de debugMode habilita el modo debug.
	 */

	public NER(boolean debugMode){
		setDebugMode(debugMode);
		dictionaries = new Vector<Dictionary>();
	}
	
	/**
	 * 
	 * @param debugMode El valor true de debugMode habilita el modo debug.
	 * @param dictionaries El parametro dictionaries permite establecer un diccionario creado con anterioridad.
	 */
	
	public NER(boolean debugMode,Vector<Dictionary> dictionaries){
		setDebugMode(debugMode);
		setDictionaries(dictionaries);
	}
	
	/**
	 * 
	 * @return True si se encuentra habilitado el modo debug, caso contrario false.
	 */

	public boolean isDebugModeActivated(){
		return this.debugMode;
	}
	/**
	 * 
	 * @param debugMode El parametro solicitado cambia el estado de debug de la clase
	 */
	public void setDebugMode(boolean debugMode){
		this.debugMode = debugMode;
		if (debugMode)
			Logger.getLogger(NER.class).debug("DEBUG MODE IS ACTIVATED");
	}

	/**
	 * 
	 * @param text El parametro text corresponde al texto a ser analizado
	 * @return Se retorna los chunks detectados de text por los diccionarios definidos/
	 */
	
	public Vector<Chunk> recognize(String text){
		Vector<Chunk> out = new Vector<Chunk>();
		
		for (Dictionary dictionary : dictionaries) 
			out.addAll(dictionary.recognize(text,debugMode));
				
		if (debugMode)
			System.out.println(out);
		
		return out;

	}

	/**
	 * 
	 * @return Lista de diccionarios
	 */
	public Vector<Dictionary> getDictionaries() {
		return dictionaries;
	}
	
	/**
	 * 
	 * @param dictionaries El parametro corresponde con la lista de diccionarios a establecer
	 */

	public void setDictionaries(Vector<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}
	
	/**
	 * 
	 * @param dictionary Se agrega dictionary a la lista de diccionarios a usar
	 */
	
	public void addDictionary(Dictionary dictionary){
		dictionaries.add(dictionary);
	}
}
