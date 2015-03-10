package dictionary;

import java.util.HashMap;
import java.util.Vector;

public class ExactDictionary {

	//Private Variables
	private DictionaryNode rootNode;
	private boolean caseSensitive;
	
	
	//TODO
	private HashMap<String, Byte> categoriesList;
	
	public ExactDictionary (DictionaryNode rootNode, boolean caseSensitive){
		this.setRootNode(rootNode);
		this.setCaseSensitive(caseSensitive);
	}
	
	public ExactDictionary (DictionaryNode rootNode){
		this.setRootNode(rootNode);
		this.setCaseSensitive(false);
	}

	public DictionaryNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DictionaryNode rootNode) {
		this.rootNode = rootNode;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
}
