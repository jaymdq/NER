import java.util.StringTokenizer;
import java.util.Vector;

import ner.NER;

import org.apache.log4j.Logger;

import dictionary.DictionaryEntry;
import dictionary.DictionaryIO;
import dictionary.ExactDictionary;
import entry.TextEntry;
import entry.TwitterEntry;
import filters.RetweetFilter;
import segmentation.Segmenter;
import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws TwitterException {
				
		DictionaryEntry d1 = new DictionaryEntry("Maxi Duthey",new String[]{"Persona"});
		DictionaryEntry d2 = new DictionaryEntry("Brian Caimmi",new String[]{"Persona"});
		DictionaryEntry d3 = new DictionaryEntry("Tandil",new String[]{"Localidad"});
	
		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.add(d1);
		entradas.add(d2);
		entradas.add(d3);
		
		DictionaryIO.saveToFile("prueba",DictionaryIO.DIC_EXT, entradas);
		
		entradas.clear();
		
		entradas = DictionaryIO.loadFromFile("prueba.dic");
		
		//entradas.addAll(DictionaryIO.loadPlainTextWithCategories("callesTandil.txt"));
		
		
		//TODO ver si se puede optimizar el espacio! guardando el contenido y no el objeto.
		//TODO seria pasar todo eso a texto -> aplicar Huffman
		
		ExactDictionary dic = new ExactDictionary(entradas,true,true);
		
		NER ner = new NER();
		
		ner.recognize(dic, "Maxi Duthey junto a Brian Caimmi viven en la ciudad de Tandil y trabajan en Alem al 1259.");
		
		
		//------------
	/*	
		RetweetFilter rtf = new RetweetFilter();
		
		TwitterEntry.getInstance().setFilter(rtf);

		if (TwitterEntry.getInstance().setSourceFile("tweets.txt") ){
		//if (TextEntry.getInstance().setSourceFile("textPlano.txt") ){
			String linea ;
			while ( (linea = TwitterEntry.getInstance().getTextFromStatus()) != null){
				System.out.println(linea+"\n");
			}
			
			Logger.getLogger(Main.class).info("Leí todo el archivo");
		}
		
	*/	
	}

}
