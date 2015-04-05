package dictionary;

import java.util.Arrays;
import java.util.Vector;

public class TopKAproximatedDictionary implements Dictionary {


	private Vector<DictionaryEntry> entriesList = null;
	private int top_k;
	private int n_gram;	
	private AbsTrieNode rootNode;

	public TopKAproximatedDictionary(Vector<DictionaryEntry> entriesList, int top_k, int n_gram) {
		this.setTop_k(top_k);
		this.setN_gram(n_gram);
		this.setEntriesList(entriesList);

	}



	public Vector<DictionaryEntry> getEntriesList() {
		return entriesList;
	}

	public void setEntriesList(Vector<DictionaryEntry> entriesList) {
		this.entriesList = entriesList;
		if (entriesList != null)
			this.initStructures();
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

	private void initStructures() {

		rootNode = new TrieNodeInter(null);

		for (DictionaryEntry entry : entriesList){
			String entryText = entry.getText();

			for (String s : this.splitter(entryText)){
				rootNode.addToMap(s, entry);
			}

		}

	}


	@Override
	public Vector<Chunk> recognize(String text, boolean debugMode) {
		return null;
	}

	private String[] splitter(String text){
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
}
