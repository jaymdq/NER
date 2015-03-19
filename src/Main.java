import java.util.StringTokenizer;
import java.util.Vector;

import ner.NER;

import org.apache.log4j.Logger;

import dictionary.DictionaryEntry;
import dictionary.ExactDictionary;
import entry.TextEntry;
import entry.TwitterEntry;
import filters.RetweetFilter;
import segmentation.Segmenter;
import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws TwitterException {
		
		//TODO 
		//Segmenter s = new Segmenter();
		//System.out.println(s.getListOfTokens("Hola como estas daniel, que transito           feo que hay hoy"));
		
		//DictionaryEntry d1 = new DictionaryEntry("Maxi Duthey","Persona",1.0);
		//DictionaryEntry d2 = new DictionaryEntry("Brian Caimmi","Persona",1.0);
		//DictionaryEntry d3 = new DictionaryEntry("Tandil","Localidad",1.0);
		DictionaryEntry d1 = new DictionaryEntry("a","Letra",1.0);
		DictionaryEntry d2 = new DictionaryEntry("b","Letra",1.0);
		DictionaryEntry d3 = new DictionaryEntry("a b","Letras",1.0);
		
		
		
		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.add(d1);
		entradas.add(d2);
		entradas.add(d3);
		
		ExactDictionary dic = new ExactDictionary(entradas);
		
		System.out.println(dic.toString());
		
		NER ner = new NER();
		ner.recognize(dic, "a a b");
		
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
