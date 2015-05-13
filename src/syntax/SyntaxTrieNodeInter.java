package syntax;

import java.util.HashMap;
import java.util.Vector;

import dictionary.approximatedDictionaries.AbsTrieNode;

public class SyntaxTrieNodeInter extends AbsSyntaxTrieNode {

	private HashMap<String,AbsSyntaxTrieNode> nodeMap = null;

	public SyntaxTrieNodeInter(String categoryType) {
		super(categoryType);
		nodeMap = new HashMap<String, AbsSyntaxTrieNode>();
	}

	@Override
	public void addToMap(Vector<String> categories, String resultCategory) {
		if (categories.isEmpty())
			return;
		
		String actualCategory = categories.firstElement().trim();
		AbsSyntaxTrieNode node = nodeMap.get(actualCategory);
		if (node == null){
			if (categories.size() == 1){
				node = new SyntaxTrieNodeLeaf(actualCategory);
			}else{
				node = new SyntaxTrieNodeInter(actualCategory);
			}
			nodeMap.put(actualCategory, node);
		}
		node.addToMap( new Vector<String>(categories.subList(1, categories.size())), resultCategory);

	}

	@Override
	public Vector<String> getListOfCategories(Vector<String> rule) {
		if (rule.isEmpty())
			return null;
		
		AbsSyntaxTrieNode node = nodeMap.get(rule.firstElement().trim());
		return (node == null) ? null : node.getListOfCategories( new Vector<String>(rule.subList(1, rule.size())));
	}

}
