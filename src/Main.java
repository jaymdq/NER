import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import ner.NER;

import org.apache.log4j.Logger;

import dictionary.DictionaryEntry;
import dictionary.DictionaryIO;
import dictionary.ExactDictionary;
import dictionary.RegExMatcher;
import dictionary.RuleBasedDictionary;
import dictionary.TopKAproximatedDictionary;
import entry.TextEntry;
import entry.TwitterEntry;
import filters.RetweetFilter;
import segmentation.Segmenter;
import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws TwitterException {
			
		//Entradas agregadas por el usuario
		DictionaryEntry d1 = new DictionaryEntry("Maxi Duthey",new String[]{"Persona"});
		DictionaryEntry d2 = new DictionaryEntry("Brian Caimmi",new String[]{"Persona"});
		DictionaryEntry d3 = new DictionaryEntry("Tandil",new String[]{"Localidad"});		
		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.add(d1);
		entradas.add(d2);
		entradas.add(d3);
		
		//Entradas levantadas desde archivo
		Set<DictionaryEntry> entries = new HashSet<DictionaryEntry>();
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/callesTandil.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/colores.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/marcasDeAutos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/modelosDeAutos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/rutas.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/marcasDeMotos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/corpusDeVehiculos.txt"));
		entradas.addAll(entries);
		
		//Creación de Diccionarios
		ExactDictionary dic = new ExactDictionary(entradas,false,false);
		
		RuleBasedDictionary dic2 = new RuleBasedDictionary();
		dic2.addMatcher(new RegExMatcher("[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})","Mail"));
		dic2.addMatcher(new RegExMatcher("[0-9]+","Numero"));
		
		TopKAproximatedDictionary dic3 = new TopKAproximatedDictionary(entradas, 2, 2, 1);
				
		//Creación del NER
		NER ner = new NER(false);
		ner.addDictionary(dic);
		ner.addDictionary(dic2);
		ner.addDictionary(dic3);
		System.out.println(ner.recognize("Maxi Duthey junto a Brian Caimmi viven en la ciudad de Tandil y trabajan en Alem al 1259."));
		System.out.println(ner.recognize("Un menor herido al chocar dos camionetas en la Ruta 30 y Jujuy http://ow.ly/KDOGq"));
		
		
		
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
