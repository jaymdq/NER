package dictionary.approximatedDictionaries;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import segmentation.Segmenter;
import twitter4j.Logger;
import dictionary.Dictionary;
import dictionary.chunk.Chunk;
import dictionary.chunk.comparator.ChunkComparatorByScore;
import dictionary.chunk.score.Score;
import dictionary.dictionaryentry.DictionaryEntry;
import dictionary.dictionaryentry.DictionaryEntryWithDistance;


public class ApproximatedDictionary implements Dictionary {
	
	// Variables
	
	private Vector<DictionaryEntry> entriesList = null;
	private double lowerLimit;
	private int n_gram;	
	private int threshold;
	private AbsTrieNode rootNode;
	private boolean caseSensitive;

	// Constructors
	
	public ApproximatedDictionary(Vector<DictionaryEntry> entriesList, double lowerLimit, int n_gram, int threshold, boolean caseSensitive) {
		this.setTop_k(lowerLimit);
		this.setN_gram(n_gram);
		this.setThreshold(threshold);

		//Keep this in the bottom of the method
		this.setEntriesList(entriesList);
	}

	// Getters And Setters
	
	public Vector<DictionaryEntry> getEntriesList() {
		return entriesList;
	}

	public void setEntriesList(Vector<DictionaryEntry> entriesList) {
		this.entriesList = entriesList;
		if (entriesList != null){
			this.initStructures();
		}
	}

	public double getTop_k() {
		return lowerLimit;
	}

	public void setTop_k(double top_k2) {
		this.lowerLimit = top_k2;
	}

	public int getN_gram() {
		return n_gram;
	}

	public void setN_gram(int n_gram) {
		this.n_gram = n_gram;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	// Methods
	
	private String[] split(String text){
		String[] out = new String[text.length()-this.n_gram+1];
		for(int i=0; i <= (text.length()-this.n_gram); i++){
			String tmp = "";
			for(int j=0; j < this.n_gram; j++){
				tmp += text.charAt(j+i);
			}
			out[i] = tmp;
		}
		return out;
	}

	private void initStructures() {
		rootNode = new TrieNodeInter(null);

		for (DictionaryEntry entry : entriesList){

			if (!caseSensitive){
				entry.setText(entry.getText().toLowerCase());
			}	
			String entryText = entry.getText();
			if (entryText.length() >= n_gram){
				String[] ngrams = this.split(entryText);
				for (String ngram : ngrams){
					rootNode.addToMap(ngram, entry);
				}
			}

		}
	}

	private Integer distance(String s0, String s1){
		int len0 = s0.length() + 1;                                                     
		int len1 = s1.length() + 1;                                                     

		// the array of distances                                                       
		int[] cost = new int[len0];                                                     
		int[] newcost = new int[len0];                                                  

		// initial cost of skipping prefix in String s0                                 
		for (int i = 0; i < len0; i++) 
			cost[i] = i;                                     

		// dynamically computing the array of distances                                  

		// transformation cost for each letter in s1                                    
		for (int j = 1; j < len1; j++) {                                                
			// initial cost of skipping prefix in String s1                             
			newcost[0] = j;                                                             

			// transformation cost for each letter in s0                                
			for(int i = 1; i < len0; i++) {                                             
				// matching current letters in both strings                             
				int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;             

				// computing cost for each transformation                               
				int cost_replace = cost[i - 1] + match;                                 
				int cost_insert  = cost[i] + 1;                                         
				int cost_delete  = newcost[i - 1] + 1;                                  

				// keep minimum cost                                                    
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}                                                                           

			// swap cost/newcost arrays                                                 
			int[] swap = cost; 
			cost = newcost; 
			newcost = swap;                          
		}                                                                               

		// the distance is the cost for transforming all letters in both strings        
		return cost[len0 - 1];                                  
	}


	private Vector<DictionaryEntryWithDistance> calculateAproximity(String text){
		if (text.length() >= n_gram){
			//Split the text	
			String[] qgrams = split(text);
			Set<DictionaryEntry> invLists = new HashSet<DictionaryEntry>();

			for (String s : qgrams){
				Vector<DictionaryEntry> invList = rootNode.getListOfDictionaryEntries(s);
				if (invList != null)
					invLists.addAll(invList);
			}

			//Aca ahora se tiene que calcular la aproximidad a todas las palabras de las listas invertidas
			Vector<DictionaryEntryWithDistance> out = new Vector<DictionaryEntryWithDistance>();
			for (DictionaryEntry entry : invLists){
				Integer distanceCalculated = distance(text,entry.getText());
				if (distanceCalculated <= threshold){
					out.add(new DictionaryEntryWithDistance(entry,distanceCalculated));
				}

			}

			return out;
		}
		return null;
	}


	@Override
	public Vector<Chunk> recognize(String text, boolean debugMode){
		if (debugMode){
			Logger.getLogger(ApproximatedDictionary.class).info("Case Sensitive: " + caseSensitive + ".");
			Logger.getLogger(ApproximatedDictionary.class).info("Lower Limit: " + lowerLimit + ".");
			Logger.getLogger(ApproximatedDictionary.class).info("N-Gram: " + n_gram + ".");
			Logger.getLogger(ApproximatedDictionary.class).info("Threshold: " + threshold + ".");
			Logger.getLogger(ApproximatedDictionary.class).info("Recognition Started");			
		}
		
		Vector<Chunk> out = new Vector<Chunk>();

		Segmenter segmenter = new Segmenter(text,caseSensitive);

		Vector<String> tokens = new Vector<String>();
		Vector<Integer> startsPositions = new Vector<Integer>();
		Vector<Integer> endsPositions = new Vector<Integer>();

		String token = segmenter.getNextToken();
		while( token != null ){
			tokens.add(token);
			startsPositions.add(segmenter.getLastTokenStartPosition());
			endsPositions.add(segmenter.getLastTokenEndPosition());
			token = segmenter.getNextToken();
		}

		for (int i = 0; i < tokens.size(); i++){
			for(int j = i; j < tokens.size(); j++){

				Vector<DictionaryEntryWithDistance> results = new Vector<DictionaryEntryWithDistance>();

				token = text.substring(startsPositions.elementAt(i), endsPositions.elementAt(j));
				if(!caseSensitive)
					token = token.toLowerCase();
				Vector<DictionaryEntryWithDistance> possibleOnes = calculateAproximity(token);
				if (possibleOnes != null)
					results.addAll(possibleOnes);

				for (DictionaryEntryWithDistance entry : results){
					for (String category : entry.getCategory()){
						Chunk toAdd = new Chunk(startsPositions.elementAt(i),endsPositions.elementAt(i),category,text,Score.getInstance().getAproximatedScore(entry.getDistance(), entry.getText().length()));

						if (out.isEmpty())
							out.add(toAdd);

						boolean entered = false;
						boolean needToAdd = false;
						for (Chunk chunk : out){
							if (chunk.getText().equals(toAdd.getText()) && chunk.start() == toAdd.start() && chunk.end() == toAdd.end() && toAdd.getCategoryType().equals(chunk.getCategoryType())){
								if (toAdd.getScore() >= chunk.getScore()){
									out.set(out.indexOf(chunk), toAdd);
									needToAdd = false;
								}
								entered = true;
							}else
								needToAdd = true;
						}
						if (!entered && needToAdd)
							out.add(toAdd);
					}
				}	
			}	
		}

		Collections.sort(out, new ChunkComparatorByScore());
		Vector<Chunk> realOut = new Vector<Chunk>();

		for (Chunk chunk : out){
			if (chunk.getScore() >= lowerLimit){
				realOut.add(chunk);
			}else
				break;
		}
		
		if (debugMode){
			Logger.getLogger(ApproximatedDictionary.class).info("Chunks Found:");
			for (Chunk chunk : realOut)
				Logger.getLogger(ApproximatedDictionary.class).info(chunk.toString());
			Logger.getLogger(ApproximatedDictionary.class).info("Aproximated Dictionary Finished\n");
		}
		return realOut;
	}

}
