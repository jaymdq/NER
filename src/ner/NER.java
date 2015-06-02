package ner;

import java.util.Vector;

import org.apache.log4j.Logger;

import preprocess.PreProcess;
import syntax.SyntaxChecker;
import dictionary.Chunk;
import dictionary.Dictionary;

public class NER {

	private Vector<Dictionary> dictionaries;
	private PreProcess preProcess;
	private SyntaxChecker syntaxChecker;
	private boolean debugMode;
	private boolean doPreProcess;

	/**
	 * Constructor vacio, no se habilita el modo debug.
	 */
	public NER(){
		setDebugMode(false);
		this.dictionaries = new Vector<Dictionary>();
		this.syntaxChecker = null;
		this.preProcess = null;
		this.doPreProcess = false;
	}

	/**
	 * 
	 * @param debugMode El valor true de debugMode habilita el modo debug.
	 */
	public NER(boolean debugMode){
		setDebugMode(debugMode);
		this.dictionaries = new Vector<Dictionary>();
		this.syntaxChecker = null;
		this.preProcess = null;
		this.doPreProcess = false;
	}

	/**
	 * 
	 * @param debugMode El valor true de debugMode habilita el modo debug.
	 * @param dictionaries El parametro dictionaries permite establecer un diccionario creado con anterioridad.
	 */
	public NER(boolean debugMode,Vector<Dictionary> dictionaries, SyntaxChecker syntaxChecker, PreProcess preProcess, boolean doPreProcess){
		setDebugMode(debugMode);
		setDictionaries(dictionaries);
		setSyntaxChecker(syntaxChecker);
		setPreProcess(preProcess);
		
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
			Logger.getLogger(NER.class).info("DEBUG MODE IS ACTIVATED\n");
	}

	/**
	 * 
	 * @param text El parametro text corresponde al texto a ser analizado
	 * @return Se retorna los chunks detectados de text por los diccionarios definidos/
	 */
	public Vector<Chunk> recognize(String text){
		Vector<Chunk> out = new Vector<Chunk>();

		if (debugMode){
			Logger.getLogger(NER.class).info("Text: " + text + "\n");			
		}
		
		if (preProcess != null){
			if (doPreProcess){
				text = preProcess.execute(text);
				Logger.getLogger(NER.class).info("Post PreProcess Text: " + text + "\n");
			}
		}
		
		for (Dictionary dictionary : dictionaries) 
			out.addAll(dictionary.recognize(text,debugMode));

		if (debugMode){
			Logger.getLogger(NER.class).info("Pre Syntax Checker - Chunks");
			Logger.getLogger(NER.class).info(out.toString() + "\n");
		}
		
		
		if (syntaxChecker != null){
			out = syntaxChecker.joinChunks(out, text);
			
			if (debugMode){
				Logger.getLogger(NER.class).info("Post Syntax Checker - Chunks");
				Logger.getLogger(NER.class).info(out.toString() + "\n");
			}
		}

		if (debugMode)
			Logger.getLogger(NER.class).info("NER Finished\n");
		
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
	
	/**
	 * 
	 * @param syntaxChecker Se define el SyntaxChecker utilizado en la clase NER
	 */
	public void setSyntaxChecker(SyntaxChecker syntaxChecker){
		this.syntaxChecker = syntaxChecker;
	}
	
	/**
	 * 
	 * @return SyntaxChecker de la clase NER
	 */
	public SyntaxChecker getSyntaxChecker(){
		return this.syntaxChecker;
	}

	public PreProcess getPreProcess() {
		return preProcess;
	}

	public void setPreProcess(PreProcess preProcess) {
		this.preProcess = preProcess;
	}

	public boolean DoPreProcess() {
		return doPreProcess;
	}

	public void setDoPreProcess(boolean doPreProcess) {
		this.doPreProcess = doPreProcess;
	}

	public boolean DebugMode() {
		return debugMode;
	}
	
	
}
