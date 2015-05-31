import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import dictionary.DictionaryEntry;
import dictionary.approximatedDictionaries.AproximatedDictionary;
import dictionary.exactDictionaries.ExactDictionary;
import dictionary.io.DictionaryIO;
import dictionary.ruleBasedDictionaries.RegExMatcher;
import dictionary.ruleBasedDictionaries.RuleBasedDictionary;
import ner.NER;
import syntax.SyntaxChecker;
import topic.TopicSearcher;
import utils.Pair;


public class Main2 {

	public static void main(String[] args) {

		//Tópico
		String topic = "";

		//Texto
		//StreamPlainTextWorker streamPlainTextWorker = new StreamPlainTextWorker("tweets","");


		//TopicSearcher topicSearcher = new TopicSearcher();

		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.addAll(userEntries());
		entradas.addAll(filesEntries());

		NER ner = createNER(entradas);

		
		
	}

	private static NER createNER(Vector<DictionaryEntry> entries){
		//Creación de Diccionarios

		//Diccionarios Exactos
		ExactDictionary dic1 = new ExactDictionary(entries,false,true);

		//Diccionarios basados en reglas
		RuleBasedDictionary dic2 = new RuleBasedDictionary();
		dic2.addMatcher(new RegExMatcher("[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})","Mail"));
		dic2.addMatcher(new RegExMatcher("[0-9]+","Numero"));
		dic2.addMatcher(new RegExMatcher("\\sy+","y"));

		//Diccionarios Aproximados
		AproximatedDictionary dic3 = new AproximatedDictionary(entries, 0.6, 2, 1,false);

		//Creación del SyntaxChecker
		SyntaxChecker syntaxChecker = createSyntaxChecker();

		//Creación del NER
		NER ner = new NER(false);
		ner.addDictionary(dic1);
		ner.addDictionary(dic2);
		ner.addDictionary(dic3);
		ner.setSyntaxChecker(syntaxChecker);

		return ner;
	}

	private static Vector<DictionaryEntry> userEntries(){
		//Entradas agregadas por el usuario		
		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.add(new DictionaryEntry("Maxi Duthey",new String[]{"Persona"}));
		entradas.add(new DictionaryEntry("Brian Caimmi",new String[]{"Persona"}));
		entradas.add(new DictionaryEntry("Tandil",new String[]{"Localidad"}));
		entradas.add(new DictionaryEntry("Jujuy",new String[]{"Localidad"}));

		return entradas;
	}

	private static Vector<DictionaryEntry> filesEntries(){
		//Entradas levantadas desde archivo
		Set<DictionaryEntry> entries = new HashSet<DictionaryEntry>();
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/callesTandil.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/colores.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/marcasDeAutos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/modelosDeAutos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/rutas.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/marcasDeMotos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/corpusDeVehiculos.txt"));
		return new Vector<DictionaryEntry>(entries);
	}

	private static SyntaxChecker createSyntaxChecker(){

		//TODO
		Vector<Pair<Vector<String>,String>> rules = new Vector<Pair<Vector<String>,String>>();
		Vector<String> cosas = new Vector<String>();
		cosas.add("Ruta");
		cosas.add("y");
		cosas.add("Calle");

		Pair<Vector<String>,String> rule1 = new Pair<Vector<String>, String>();
		rule1.setPair1(cosas);
		rule1.setPair2("Interseccion");
		rules.add(rule1);

		SyntaxChecker syntaxChecker = new SyntaxChecker();
		syntaxChecker.addRules(rules);


		return syntaxChecker;

	}
	
}
