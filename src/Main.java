import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import ner.NER;

import org.apache.log4j.Logger;

import dictionary.DictionaryEntry;
import dictionary.approximatedDictionaries.TopKAproximatedDictionary;
import dictionary.exactDictionaries.ExactDictionary;
import dictionary.io.DictionaryIO;
import dictionary.ruleBasedDictionaries.RegExMatcher;
import dictionary.ruleBasedDictionaries.RuleBasedDictionary;
import entry.TextEntry;
import entry.TwitterEntry;
import filters.RetweetFilter;
import segmentation.Segmenter;
import syntax.AbsSyntaxTrieNode;
import syntax.Pair;
import syntax.SyntaxTrieNodeInter;
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
		dic2.addMatcher(new RegExMatcher("\\sy+","y"));
		
		TopKAproximatedDictionary dic3 = new TopKAproximatedDictionary(entradas, 2, 2, 1);
		
		//Creación del SyntaxChecker
		
		
		//Creación del NER
		NER ner = new NER(false);
		ner.addDictionary(dic);
		ner.addDictionary(dic2);
		ner.addDictionary(dic3);
		//System.out.println(ner.recognize("Maxi Duthey junto a Brian Caimmi viven en la ciudad de Tandil y trabajan en Alem al 1259."));
		System.out.println(ner.recognize("Un menor herido al chocar dos camionetas en la Ruta 30 y Jujuy http://ow.ly/KDOGq"));
		
		//TODO vale la pena ponerle a los chucks, quien fue el diccionario que lo genero
		
		//------------
	
		/*
		RetweetFilter rtf = new RetweetFilter();
		
		TwitterEntry.getInstance().setFilter(rtf);

		if (TwitterEntry.getInstance().setSourceFile("tweets.txt") ){
		//if (TextEntry.getInstance().setSourceFile("textPlano.txt") ){
			String linea ;
			while ( (linea = TwitterEntry.getInstance().getTextFromStatus()) != null){
				System.out.println(linea+"\n");
				System.out.println(ner.recognize(linea));
			}
			
			Logger.getLogger(Main.class).info("Leí todo el archivo");
		}
		*/
	/*
		Vector<String> cosas = new Vector<String>();
		cosas.add("Calle");
		cosas.add("y");
		cosas.add("Calle");
		
		
		AbsSyntaxTrieNode root = new SyntaxTrieNodeInter(null);
		
		root.addToMap(cosas, "Interseccion");
		
		Vector<String> cosas1 = new Vector<String>();
		cosas1.add("Calle");
		cosas1.add("y");
		cosas1.add("Calle");
		
		System.out.println(root.getListOfCategories(cosas1));
		
		
		Pair<Vector<String>,String> asd = new Pair<Vector<String>, String>();
		asd.setPair1(cosas);
		asd.setPair2("Interseccion");
		
		*/
	}

}
