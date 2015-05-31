import java.util.Arrays;
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


public class Main {

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

		//Pruebas
		//System.out.println(ner.recognize("Maxi Duthey junto a Brian Caimmi viven en la ciudad de Tandil y trabajan en Alem al 1259."));
		System.out.println(ner.recognize("Un menor herido al chocar dos camionetas en la Ruta 30 y Jujuy http://ow.ly/KDOGq"));


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
		dic2.addMatcher(new RegExMatcher("[0-9]+km","Kilometro"));
		dic2.addMatcher(new RegExMatcher("[0-9]+Km","Kilometro"));
		dic2.addMatcher(new RegExMatcher("[0-9]+KM","Kilometro"));
		dic2.addMatcher(new RegExMatcher("[0-9]+kM","Kilometro"));

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

		//Here we specify the synonyms
		Vector<Vector<String>> synonyms = new Vector<Vector<String>>();
		Vector<String> synonym1 = new Vector<String>(Arrays.asList(new String[]{"Calle", "Ruta" , "Avenida", "Autopista"}));
		synonyms.add(synonym1);
		
		//Para mostrar
		//Vector<String> synonym2 = new Vector<String>(Arrays.asList(new String[]{"Kilometro", "Numero"}));
		//synonyms.add(synonym2);
		
		//In this section we create de rules
		Vector<Pair<Vector<String>,String>> rules = new Vector<Pair<Vector<String>,String>>();
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "Esquina", "Calle"},"Interseccion",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "y", "Calle"},"Interseccion",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "Entre", "Calle", "y", "Calle"},"Direccion Indeterminada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "Casi", "Calle"},"Direccion Indeterminada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "Numero"},"Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "al", "Numero"}, "Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "al", "Numero", "Entre", "Calle", "y", "Calle"},"Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Numero", "de", "Calle"},"Direccion Determinada",synonyms));
		
		//Then we create de syntaxChecker
		SyntaxChecker syntaxChecker = new SyntaxChecker();
		syntaxChecker.addRules(rules);

		return syntaxChecker;

	}

	

}
