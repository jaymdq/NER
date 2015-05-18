package dictionary.approximatedDictionaries;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import dictionary.Chunk;
import dictionary.Dictionary;
import dictionary.DictionaryEntry;
import dictionary.DictionaryEntryWithDistance;
import score.Score;
import segmentation.Segmenter;

//TODO cambiarle el nombre
public class TopKAproximatedDictionary implements Dictionary {

	private Vector<DictionaryEntry> entriesList = null;
	private int top_k;
	private int n_gram;	
	private int threshold;
	private AbsTrieNode rootNode;
	
	public TopKAproximatedDictionary(Vector<DictionaryEntry> entriesList, int top_k, int n_gram, int threshold) {
		this.setTop_k(top_k);
		this.setN_gram(n_gram);
		this.setThreshold(threshold);
		
		//Keep this in the bottom of the method
		this.setEntriesList(entriesList);

	}

	public Vector<DictionaryEntry> getEntriesList() {
		return entriesList;
	}

	public void setEntriesList(Vector<DictionaryEntry> entriesList) {
		this.entriesList = entriesList;
		if (entriesList != null){
			this.initStructures();
		}
	}

	public int getTop_k() {
		return top_k;
	}

	public void setTop_k(int top_k) {
		this.top_k = top_k;
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

			String entryText = entry.getText();
			for (String ngram : this.split(entryText)){
				rootNode.addToMap(ngram, entry);
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
		//Split el text
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


	@Override
	public Vector<Chunk> recognize(String text, boolean debugMode){
		Vector<Chunk> out = new Vector<Chunk>();

		Segmenter segmenter = new Segmenter(text,false,false);

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
				results.addAll(calculateAproximity(token));
				for (DictionaryEntryWithDistance entry : results){
					for (String category : entry.getCategory()){
						Chunk toAdd = new Chunk(startsPositions.elementAt(i),endsPositions.elementAt(i),category,text,Score.getInstance().getAproximatedScore(entry.getDistance(), entry.getText().length()));
						//TODO BRIAN
						for (Chunk chunk : out){
							if (chunk.getText().equals(toAdd.getText()) && chunk.start() == toAdd.start() && chunk.end() == toAdd.end()){
								if (toAdd.getScore() > chunk.getScore()){
									out.set(out.indexOf(chunk), toAdd);
								}
							}
						}
					}
				}	
			}	
		}
		
		return out;
	}

}
