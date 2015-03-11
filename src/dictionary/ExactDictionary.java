package dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


import segmentation.Segmenter;

public class ExactDictionary {

	//Private Variables
	private DictionaryNode rootNode;
	private boolean caseSensitive;
	private Integer maxLength; //inicializar


	public ExactDictionary (DictionaryNode rootNode, boolean caseSensitive){
		this.setRootNode(rootNode);
		this.setCaseSensitive(caseSensitive);
	}

	public ExactDictionary (DictionaryNode rootNode){
		this.setRootNode(rootNode);
		this.setCaseSensitive(false);
	}

	public ExactDictionary (Vector<DictionaryEntry> entries){
		Segmenter segmenter = new Segmenter();
		DictionaryNode root = new DictionaryNode(0);
		for (DictionaryEntry entry : entries){
			Integer length = root.addEntry(segmenter.getListOfTokens(entry.getText()),entry);
			if (length > maxLength)
				maxLength = length;			
		}
		computeSuffixes(rootNode,rootNode,new String[maxLength],0);
		this.rootNode = root;
	}

	private void computeSuffixes(DictionaryNode node, DictionaryNode rootNode, String[] tokens, int length) {
		for (int i = 1; i < length; ++i) {
			DictionaryNode suffixNode = rootNode.getChild(tokens,i,length);
			if (suffixNode == null)
				continue;
			node.setSuffixNode( suffixNode );
			break;
		}

		// second loop could start where first left off
		for (int i = 1; i < length; ++i) {
			DictionaryNode suffixNode = rootNode.getChild(tokens,i,length);
			if (suffixNode == null) 
				continue;
			if (suffixNode.getCategories().size() == 0)
				continue;
			node.setSuffixNodeWithCategory( suffixNode );
			break;
		}
		
		if (node.getChildNodes() == null) 
			return;
		
		for (Map.Entry<String,DictionaryNode> entry : node.getChildNodes().entrySet()) {
			tokens[length] = entry.getKey().toString();
			DictionaryNode dtrNode = entry.getValue();
			computeSuffixes(dtrNode,rootNode,tokens,length + 1);
		}
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
