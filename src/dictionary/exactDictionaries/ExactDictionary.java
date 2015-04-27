package dictionary.exactDictionaries;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

import dictionary.Chunk;
import dictionary.Dictionary;
import dictionary.DictionaryEntry;
import score.Score;
import segmentation.Segmenter;

public class ExactDictionary implements Dictionary {

	//Private Variables
	private DictionaryNode rootNode;
	private boolean caseSensitive;	
	private int maxLength = 0;
	private boolean allMatches;

	public ExactDictionary (DictionaryNode rootNode, boolean caseSensitive, boolean allMatches){
		this.setRootNode(rootNode);
		this.setCaseSensitive(caseSensitive);
		this.setAllMatches(allMatches);
	}

	public ExactDictionary (Vector<DictionaryEntry> entries,boolean caseSensitive, boolean allMatches){
		DictionaryNode root = new DictionaryNode(0);
		for (DictionaryEntry entry : entries){
			if (!caseSensitive)
				entry.setText(entry.getText().toLowerCase());
			Integer length = root.addEntry(Segmenter.getSegmentation(entry.getText(),true,true),entry);
			if (length > maxLength)
				maxLength = length;			
		}
		this.setRootNode(root);
		computeSuffixes(rootNode,rootNode,new String[maxLength],0);
		this.setCaseSensitive(caseSensitive);
		this.setAllMatches(allMatches);
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

	public String toString(){
		String out = "Dictionary:\n";
		out += "  Case Sensitive: " + caseSensitive + ".\n";
		out += "  All Matches: " + allMatches + ".\n";
		out += "ROOTNODE";
		return out + this.rootNode.toString();
	}

	private void computeSuffixes(DictionaryNode node, DictionaryNode rootNode, String[] tokens, int length) {
		for (int i = 1; i < length; ++i) {
			DictionaryNode suffixNode = rootNode.getChild(tokens,i,length);
			if (suffixNode == null)
				continue;
			node.setSuffixNode( suffixNode );
			break;
		}

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

	public Vector<Chunk> recognize(String text,boolean debugMode) {
		Vector<Chunk> listOfChunks = new Vector<Chunk>();
		Segmenter segmenter = new Segmenter(text,caseSensitive,true);
		CircularQueueInt queue = new CircularQueueInt(maxLength);
		DictionaryNode node = rootNode;
		String token;
		while ( ( token = segmenter.getNextToken() ) != null ) {
			int tokenStartPos = segmenter.getLastTokenStartPosition();
			int tokenEndPos = segmenter.getLastTokenEndPosition();

			if (debugMode)
				System.out.println("TOKEN=|" + token + "| START=" + tokenStartPos + " END=" + tokenEndPos);

			queue.enqueue(tokenStartPos);
			while (true) {
				DictionaryNode childNode = node.getChild(token);
				if (childNode != null) {
					node = childNode;
					break;
				}
				if (node.getSuffixNode() == null) {
					node = rootNode.getChild(token);
					if (node == null)
						node = rootNode;
					break;
				}
				node = node.getSuffixNode();

			}
			addChunk(node,queue,tokenEndPos,listOfChunks,text);
			for (DictionaryNode suffixNode = node.getSuffixWithCategoryNode();
					suffixNode != null;
					suffixNode = suffixNode.getSuffixWithCategoryNode()) {
				addChunk(suffixNode,queue,tokenEndPos,listOfChunks,text);
			}
		}
		return allMatches ? listOfChunks : restrictToLongest(listOfChunks);

	}

	private Vector<Chunk> restrictToLongest(Vector<Chunk> listOfChunks) {
		Vector<Chunk> result = new Vector<Chunk>();
		if (listOfChunks.isEmpty()) 
			return listOfChunks;
		Collections.sort(listOfChunks, LONGEST_MATCH_ORDER_COMPARATOR);
		int lastEnd = -1;
		for(Chunk chunk : listOfChunks){
			if (chunk.start() >= lastEnd){
				result.add(chunk);
				lastEnd = chunk.end();
			}	
		}
		return result;
	}

	private void addChunk(DictionaryNode node, CircularQueueInt queue, int end, Vector<Chunk> chunking,String text) {
		for (String category : node.getCategories()) {
			int start = queue.get(node.getDepth());
			Chunk chunk = new Chunk(start,end,category,text,Score.getInstance().getExactScore());
			chunking.add(chunk);
		}
	}

	public boolean isAllMatches() {
		return allMatches;
	}

	public void setAllMatches(boolean allMatches) {
		this.allMatches = allMatches;
	}

	private static final Comparator<Chunk> LONGEST_MATCH_ORDER_COMPARATOR = new Comparator<Chunk>() {
		public int compare(Chunk c1, Chunk c2) {
			if (c1.start() < c2.start()) return -1;
			if (c1.start() > c2.start()) return 1;
			if (c1.end() < c2.end()) return 1;
			if (c1.end() > c2.end()) return -1;
			return c1.type().compareTo(c2.type());
		}
	};

}
