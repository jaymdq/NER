package dictionary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import segmentation.TokenList;



public class DictionaryNode {

	private HashMap<String, DictionaryNode> childNodes;
	private Vector<String> categories;
	private Integer depth;

	private DictionaryNode suffixNode;
	private DictionaryNode suffixNodeWithCategory;

	//TODO
	public DictionaryNode(Integer depth){
		this.depth = depth;
		this.childNodes = null;
		this.categories = new Vector<String>();
	}

	public Integer getDepth(){
		return depth;
	}

	public DictionaryNode getChild(String key) {
		return childNodes.get(key);
	}

	public DictionaryNode getChild(String[] tokens, int start, int end) {
		DictionaryNode node = this;
		for (int i = start; i < end && node != null; ++i)
			node = node.getChild(tokens[i]);
		return node;
	}

	public DictionaryNode getOrCreateChild(String key) {
		DictionaryNode existingDaughter = getChild(key);
		if (existingDaughter != null) 
			return existingDaughter;
		DictionaryNode newChild = new DictionaryNode(getDepth() + 1);
		if (childNodes == null)
			childNodes = new HashMap<String,DictionaryNode>(2);
		childNodes.put(key,newChild);
		return newChild;
	}

	public Integer addEntry(TokenList listOfTokens, DictionaryEntry entry) {
		DictionaryNode node = this;
		String token;
		for (int i = 0; i < listOfTokens.size(); i++){
			token = listOfTokens.getTokenAt(i).toString();
			node = node.getOrCreateChild(token);
		}
		node.addEntry(entry);
		return node.getDepth();
	}

	public void addEntry(DictionaryEntry entry) {
		categories.add(entry.getCategory());
	}

	public void setSuffixNode(DictionaryNode suffixNode) {
		this.suffixNode = suffixNode;
	}

	public void setSuffixNodeWithCategory(DictionaryNode suffixNodeWithCategory) {
		this.suffixNodeWithCategory = suffixNodeWithCategory;
	}

	public Vector<String> getCategories(){
		return categories;
	}
	
	public  HashMap<String, DictionaryNode> getChildNodes(){
		return childNodes;
	}


}
