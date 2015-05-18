package syntax;

import java.util.Vector;

public class SyntaxTrieNodeLeaf extends AbsSyntaxTrieNode {
	
	private Vector<String> categories;
	
	public SyntaxTrieNodeLeaf(String categoryType) {
		super(categoryType);
		this.categories = new Vector<String>();
	}

	@Override
	public void addToMap(Vector<String> categories, String resultCategory) {
		if (! categories.isEmpty() )
			return;
		this.categories.add(resultCategory);
	}

	@Override
	public Vector<String> getListOfCategories(Vector<String> rule) {
		if (! rule.isEmpty() )
			return null;
		return this.categories;
	}

}
