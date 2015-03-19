package ner;

import dictionary.ExactDictionary;

public class NER {

	/*TODO
	 * La idea seria meter aca toda la funcionalidad de la entrada, procesamiento , cambio de estrategias y la salida
	 */
	
	
	//TODO puede ser que haya un diccionario abstracto.. o hacer varios recognize
	public void recognize(ExactDictionary dictionary, String text){
		dictionary.recognize(text);
		
		
	}
	
}
