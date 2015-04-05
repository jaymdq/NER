package dictionary;

import java.util.HashMap;
import java.util.Vector;

public class TrieNodeInter extends AbsTrieNode {
	private HashMap<Character,AbsTrieNode> nodeMap = null;
	
	public TrieNodeInter(Character character) {
		super(character);
		nodeMap = new HashMap<Character, AbsTrieNode>();
	}

	@Override
	public Vector<DictionaryEntry> getListOfDictionaryEntries(String text) {
		AbsTrieNode node = nodeMap.get(text.charAt(0));
		return (node == null) ? null : node.getListOfDictionaryEntries(text.substring(1, text.length()));
	}

	@Override
	public void addToMap(String text, DictionaryEntry dictionaryEntry) {
		AbsTrieNode node = nodeMap.get(text.charAt(0));
		if (node == null){
			if (text.length() == 1){
				node = new TrieNodeLeaf(text.charAt(0));
			}else{
				node = new TrieNodeInter(text.charAt(0));
			}
			nodeMap.put(text.charAt(0), node);
		}
		node.addToMap(text.substring(1, text.length()), dictionaryEntry);

	}

}
