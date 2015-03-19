package dictionary;

import java.util.HashMap;
import java.util.Vector;


public class DictionaryNode {

	private HashMap<String, DictionaryNode> childNodes;
	private Vector<String> categories;
	private Integer depth;
	private static int autoId = 0;
	private int id;


	private DictionaryNode suffixNode;
	private DictionaryNode suffixNodeWithCategory;

	//TODO
	public DictionaryNode(Integer depth){
		this.depth = depth;
		this.childNodes = new HashMap<String,DictionaryNode>();
		this.categories = new Vector<String>();
		this.id = autoId;
		autoId++;
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
		DictionaryNode existingChild = getChild(key);
		if (existingChild != null) 
			return existingChild;
		DictionaryNode newChild = new DictionaryNode(getDepth() + 1);
		childNodes.put(key,newChild);
		return newChild;
	}

	public Integer addEntry(Vector<String> listOfTokens, DictionaryEntry entry) {
		DictionaryNode node = this;
		for (String token : listOfTokens)
			node = node.getOrCreateChild(token);
		node.addEntry(entry);
		return node.getDepth();
	}

	public void addEntry(DictionaryEntry entry) {
		for (String category : entry.getCategory() )
			categories.add(category);
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

	public DictionaryNode getSuffixNode(){
		return this.suffixNode;
	}

	public DictionaryNode getSuffixWithCategoryNode(){
		return this.suffixNodeWithCategory;
	}

	public String toString(){
		String out = "";
		out += "_ID: " + this.getId();
		for(int i = 0; i < this.categories.size(); ++i ){
			out = indent(out);
			out += "cat " + i + " = " + this.categories.get(i);
		}
		if (this.suffixNode != null){
			out = indent(out);
			out += "suffixNode = " + this.suffixNode.getId();
		}
		if (this.suffixNodeWithCategory != null){
			out = indent(out);
			out += "suffixNodeWithCat = " + this.suffixNodeWithCategory.getId();
		}
		if ( this.childNodes.isEmpty() ) 
			return out;
		for (String token : this.childNodes.keySet()) {
			out = indent(out);
			out += token;
			out += this.getChild(token).toString();
		}
		return out;
	}

	public int getId() {
		return this.id;
	}

	private String indent(String auxText) {
		auxText += "\n";
		for (int i = 0; i < this.depth-1; ++i)
			auxText += "      ";
		if (this.depth > 0) auxText += "  ╚══»";
		return auxText;
	}


}
