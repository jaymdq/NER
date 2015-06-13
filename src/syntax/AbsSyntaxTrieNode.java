package syntax;

import java.util.Vector;

public abstract class AbsSyntaxTrieNode {

	// Variables
	
	protected String categoryType;
	
	// Constructors
	
	public AbsSyntaxTrieNode(String categoryType){
		super();
		this.categoryType = categoryType;
	}

	// Getters And Setters
	
	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	// Methods
	
	public abstract Vector<String> getListOfCategories(Vector<String> rule);
	public abstract void addToMap(Vector<String> categories, String resultCategory);
	
}
