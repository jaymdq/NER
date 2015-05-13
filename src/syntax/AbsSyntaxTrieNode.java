package syntax;

import java.util.Vector;

public abstract class AbsSyntaxTrieNode {

	protected String categoryType;
	
	public AbsSyntaxTrieNode(String categoryType){
		super();
		this.categoryType = categoryType;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	public abstract Vector<String> getListOfCategories(Vector<String> rule);
	public abstract void addToMap(Vector<String> categories, String resultCategory);
	
}
