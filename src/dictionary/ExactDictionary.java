package dictionary;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


import segmentation.Segmenter;

public class ExactDictionary {

	//Private Variables
	private DictionaryNode rootNode;
	private boolean caseSensitive;
	private int maxLength = 0;


	public ExactDictionary (DictionaryNode rootNode, boolean caseSensitive){
		this.setRootNode(rootNode);
		this.setCaseSensitive(caseSensitive);
	}

	public ExactDictionary (DictionaryNode rootNode){
		this.setRootNode(rootNode);
		this.setCaseSensitive(false);
	}

	public ExactDictionary (Vector<DictionaryEntry> entries){
		DictionaryNode root = new DictionaryNode(0);
		for (DictionaryEntry entry : entries){
			Integer length = root.addEntry(Segmenter.getSegmentation(entry.getText()),entry);
			if (length > maxLength)
				maxLength = length;			
		}
		this.setRootNode(root);
		computeSuffixes(rootNode,rootNode,new String[maxLength],0);
		this.setCaseSensitive(false); //TODO
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
		return this.rootNode.toString();
	}

	public Vector<Chunk> recognize(String text) {
		Vector<Chunk> listOfChunks = new Vector<Chunk>();
		Segmenter segmenter = new Segmenter(text);

		CircularQueueInt queue = new CircularQueueInt(maxLength);

		DictionaryNode node = rootNode;
		String token;

		while ( ( token = segmenter.getNextToken() ) != null ) {
			int tokenStartPos = segmenter.getLastTokenStartPosition();
			int tokenEndPos = segmenter.getLastTokenEndPosition();

			System.out.println("token=|" + token + "| start=" + tokenStartPos + " |end=" + tokenEndPos);

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

			emit(node,queue,tokenEndPos,listOfChunks,text);

			for (DictionaryNode suffixNode = node.getSuffixWithCategoryNode();
					suffixNode != null;
					suffixNode = suffixNode.getSuffixWithCategoryNode()) {
				emit(suffixNode,queue,tokenEndPos,listOfChunks,text);
			}
		}
		return restrictToLongest(listOfChunks);

	}

	private Vector<Chunk> restrictToLongest(Vector<Chunk> listOfChunks) {
		Vector<Chunk> result = new Vector<Chunk>();
		if (listOfChunks.isEmpty()) 
			return listOfChunks;

		//Chunk[] chunks = chunkSet.<Chunk>toArray(EMPTY_CHUNK_ARRAY);
		//Arrays.<Chunk>sort(chunks,LONGEST_MATCH_ORDER_COMPARATOR);

		Collections.sort(listOfChunks, LONGEST_MATCH_ORDER_COMPARATOR);

		int lastEnd = -1;
		for (int i = 0; i < listOfChunks.size(); ++i) {
			if (listOfChunks.elementAt(i).start() >= lastEnd) {
				result.add(listOfChunks.elementAt(i));
				lastEnd = listOfChunks.elementAt(i).end();
			}
		}
		return result;
	}

	void emit(DictionaryNode node, CircularQueueInt queue, int end, Vector<Chunk> chunking,String text) {
		for (String category : node.getCategories()) {
			int start = queue.get(node.getDepth());
			Chunk chunk = new Chunk(start,end,category,text);
			chunking.add(chunk);
		}
	}

	public static final Comparator<Chunk> LONGEST_MATCH_ORDER_COMPARATOR
	= new Comparator<Chunk>() {
		public int compare(Chunk c1, Chunk c2) {
			if (c1.start() < c2.start()) return -1;
			if (c1.start() > c2.start()) return 1;
			if (c1.end() < c2.end()) return 1;
			if (c1.end() > c2.end()) return -1;
			return c1.type().compareTo(c2.type());
		}
	};

}
