package topic;

import java.util.Vector;

import ner.NER;
import dictionary.Chunk;

public class TopicSearcher {

	private String topic;
	private NER ner;
	//TODO aca en vez de tener textos deberia tener los tweets en todo caso o leerlos internamente
	private Vector<String> texts;
	
	public TopicSearcher(String topic, Vector<String> texts, NER ner){
		this.setTopic(topic);
		this.setTexts(texts);
		this.setNer(ner);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public NER getNer() {
		return ner;
	}

	public void setNer(NER ner) {
		this.ner = ner;
	}

	public Vector<String> getTexts() {
		return texts;
	}

	public void setTexts(Vector<String> texts) {
		this.texts = texts;
	}
	
	//TODO Esto en realidad tendria que devolver un vector de tweets juntos con una confianza
	//la confianza determinaria con que porcentaje de certeza se sabe que cada tweet pertenece al conjunto
	//Ver además si es necesario establecer un limite inferior para determinar que un tweet pertenezca al conjunto
	//tipo: si vos tenes un tweet que nombra un auto, no incluirlo porque si al conjunto de "transito" asi porque si.
	public Vector<String> searchByTopic(){
		Vector<String> out = new Vector<String>();
		
		for (String text : texts){
			Vector<Chunk> chunks = ner.recognize(text);
			
		}
		
		return out;
	}
	
	
}
