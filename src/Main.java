import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import ner.NER;
import preprocess.PreProcess;
import syntax.SyntaxChecker;
import utils.Pair;
import configuration.ApproximatedDictionaryConfigurator;
import configuration.ExactDictionaryConfigurator;
import configuration.RuleBasedConfigurator;
import dictionary.approximatedDictionaries.ApproximatedDictionary;
import dictionary.chunk.AbsChunk;
import dictionary.chunk.Chunk;
import dictionary.chunk.ChunkEvent;
import dictionary.dictionaryEntry.DictionaryEntry;
import dictionary.exactDictionaries.ExactDictionary;
import dictionary.io.DictionaryIO;
import dictionary.ruleBasedDictionaries.RegExMatcher;
import dictionary.ruleBasedDictionaries.RuleBasedDictionary;
import event.twevent.FixedWindow;
import event.twevent.Twevent;
import tweetsAnalyzer.arff.ArffGenerator;


public class Main {

	public static void main(String[] args) {

		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.addAll(userEntries());
		entradas.addAll(filesEntries());

		NER ner = createNER(entradas);

		//Pruebas
		//System.out.println(ner.recognize("Maxi Duthey junto a Brian Caimmi viven en la ciudad de Tandil y trabajan en Alem al 1259"));
		//System.out.println(ner.recognize("Un menor herido al chocar dos camionetas en la Ruta 30 y Jujuy http://ow.ly/KDOGq"));
	
		Vector<String> tweets = new Vector<String>();
		tweets.add("Siempre estuvo el herido pibe");
		tweets.add("Siempre estuvo herido el pibe no sabia que poner");
		tweets.add("Herido herido hitaso");
		
		//Experimental
		//Twevent tw = new Twevent(ner,tweets);
		//Vector<ChunkEvent> tmp = tw.detectEvents(true, true);
		Vector< Pair< String, Vector<Chunk> > > toAnalyze = new Vector< Pair< String, Vector<Chunk> > >();
		for(String tweet: tweets)
			toAnalyze.add( new Pair< String, Vector<Chunk> >(tweet, ner.recognize(tweet)) );
		
		Twevent tw = new Twevent(new FixedWindow(toAnalyze.size(),0.2), 0.0, 0.2);
		Vector<ChunkEvent> vector_evento = tw.detectEvents(toAnalyze,true);
		
		Vector< Vector<AbsChunk> > chunkList = new Vector< Vector<AbsChunk> >();
		for(Pair< String, Vector<Chunk> > pair : toAnalyze){
			Vector<AbsChunk> vector_tmp = new Vector<AbsChunk>(); 
			vector_tmp.addAll(pair.getPair2());
			vector_tmp.addAll(vector_evento);
			chunkList.add(vector_tmp);
		}
		ArffGenerator arffGenerator = new ArffGenerator("main.test");
		arffGenerator.execute(chunkList);
	}

	private static NER createNER(Vector<DictionaryEntry> entries){

		//Creación del PreProcess
		PreProcess preProcess = new PreProcess();
		preProcess.addRule(new Pair<String, String>("(\\D)([\\.,])(\\D)?","$1 $2 $3"));
		preProcess.addRule(new Pair<String, String>(" {2,}"," "));
		

		//Creación de Diccionarios
		//Diccionarios Exactos
		//ExactDictionary dic1 = new ExactDictionary(entries,false,true);
		ExactDictionaryConfigurator eDC = new ExactDictionaryConfigurator(ExactDictionary.class.getName(),entries);
		ExactDictionary dic1 = (ExactDictionary) eDC.configure("-C -a  ");
		
		
		//Diccionarios basados en reglas
		RuleBasedConfigurator rBC = new RuleBasedConfigurator(RuleBasedDictionary.class.getName(), entries, null);
		RuleBasedDictionary dic2 = (RuleBasedDictionary) rBC.configure("");
		dic2.addMatcher(new RegExMatcher("[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})","Mail"));
		dic2.addMatcher(new RegExMatcher("[0-9]+","numero"));
		dic2.addMatcher(new RegExMatcher("\\sy+","y"));
		dic2.addMatcher(new RegExMatcher("al","al"));
		dic2.addMatcher(new RegExMatcher("la","la"));
		dic2.addMatcher(new RegExMatcher("esquina","esquina"));
		dic2.addMatcher(new RegExMatcher("entre","entre"));
		dic2.addMatcher(new RegExMatcher("casi","casi"));
		dic2.addMatcher(new RegExMatcher("de","de"));

		//Diccionarios Aproximados
		//ApproximatedDictionary dic3 = new ApproximatedDictionary(entries, 0.6, 2, 1,false);
		ApproximatedDictionaryConfigurator aDC = new ApproximatedDictionaryConfigurator(ApproximatedDictionaryConfigurator.class.getName(), entries);
		ApproximatedDictionary dic3 = (ApproximatedDictionary) aDC.configure("-l 0.6 -n 2 -t 1");
		
		//Creación del SyntaxChecker
		SyntaxChecker syntaxChecker = createSyntaxChecker();		

		//Creación del NER
		NER ner = new NER(true);
		ner.addDictionary(dic1);
		ner.addDictionary(dic2);
		ner.addDictionary(dic3);
		ner.setSyntaxChecker(syntaxChecker);
		ner.setPreProcess(preProcess);
		ner.setDoPreProcess(true);
		ner.setToLowerCase(true);

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
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/tipo_calle.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/tipo_persona.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/evento_demora.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/evento_accidente.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/calles_BA.txt"));

		return new Vector<DictionaryEntry>(entries);
	}

	private static SyntaxChecker createSyntaxChecker(){

		//Here we specify the synonyms
		Vector<Vector<String>> synonyms = new Vector<Vector<String>>();
		Vector<String> synonym1 = new Vector<String>(Arrays.asList(new String[]{"Calle", "Ruta" , "Avenida", "Autopista"}));
		synonyms.add(synonym1);

		//In this section we create the rules
		Vector<Pair<Vector<String>,String>> rules = new Vector<Pair<Vector<String>,String>>();
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "esquina", "Calle"},"Interseccion",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "y", "Calle"},"Interseccion",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "entre", "Calle", "y", "Calle"},"Direccion Indeterminada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "casi", "Calle"},"Direccion Indeterminada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "numero"},"Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "al", "Numero"}, "Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "a", "la", "Numero"}, "Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "al", "Numero", "Entre", "Calle", "y", "Calle"},"Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"numero", "de", "Calle"},"Direccion Determinada",synonyms));

		//Then we create the syntaxChecker
		SyntaxChecker syntaxChecker = new SyntaxChecker();
		syntaxChecker.addRules(rules);

		return syntaxChecker;

	}

}
