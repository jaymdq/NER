package syntax;

import java.util.HashMap;
import java.util.Vector;

public class SyntaxTrieNodeInter extends AbsSyntaxTrieNode {

	private HashMap<String,AbsSyntaxTrieNode> nodeMap = null;

	private Vector<String> categoriesResult = null;
	
	public SyntaxTrieNodeInter(String categoryType) {
		super(categoryType);
		nodeMap = new HashMap<String, AbsSyntaxTrieNode>();
	}
	
	public SyntaxTrieNodeInter(String categoryType, String categoryResult) {
		super(categoryType);
		nodeMap = new HashMap<String, AbsSyntaxTrieNode>();
		this.categoriesResult = new Vector<String>();
		this.categoriesResult.addElement(categoryResult);
	}

	@Override
	public void addToMap(Vector<String> categories, String resultCategory) {
		if (categories.isEmpty())
			return;
		
		String actualCategory = categories.remove(0).trim();
		AbsSyntaxTrieNode node = nodeMap.get(actualCategory);
		if (node == null){
			if (categories.size() == 0){
				node = new SyntaxTrieNodeInter(actualCategory, resultCategory);
			}else{
				node = new SyntaxTrieNodeInter(actualCategory);
			}
			nodeMap.put(actualCategory, node);
		}
		node.addToMap( new Vector<String>(categories), resultCategory);

	}

	@Override
	public Vector<String> getListOfCategories(Vector<String> rule) {
		if (rule.isEmpty())
			return this.categoriesResult;
		AbsSyntaxTrieNode node = nodeMap.get(rule.remove(0).trim());
		
		return (node == null) ? null : node.getListOfCategories( new Vector<String>(rule));
	}

}
