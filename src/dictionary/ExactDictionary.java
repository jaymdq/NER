package dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ChunkFactory;
import com.aliasi.chunk.ChunkingImpl;
import com.aliasi.dict.ExactDictionaryChunker.CircularQueueInt;
import com.aliasi.dict.ExactDictionaryChunker.ScoredCat;
import com.aliasi.dict.ExactDictionaryChunker.TrieNode;

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

	public void recognize(String text) {
		Segmenter segmenter = new Segmenter(text);

		CircularQueueInt queue = new CircularQueueInt(mMaxPhraseLength);

		DictionaryNode node = rootNode;
		String token;

		while ( ( token = segmenter.getNextToken() ) != null ) {
			int tokenStartPos = segmenter.getLastTokenStartPosition();
			int tokenEndPos = segmenter.getLastTokenEndPosition();

			// System.out.println("token=|" + token + "| start=" + tokenStartPos + " |end=" + tokenEndPos);

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

			emit(node,queue,tokenEndPos,chunking);

			for (TrieNode suffixNode = node.mSuffixNodeWithCategory;
					suffixNode != null;
					suffixNode = suffixNode.mSuffixNodeWithCategory) {
				emit(suffixNode,queue,tokenEndPos,chunking);
			}
		}
		return mReturnAllMatches ? chunking : restrictToLongest(chunking);

	}

	void emit(TrieNode node, CircularQueueInt queue, int end,
			ChunkingImpl chunking) {
		ScoredCat[] scoredCats = node.mCategories;
		for (int i = 0; i < scoredCats.length; ++i) {
			int start = queue.get(node.depth());
			String type = scoredCats[i].mCat;
			double score = scoredCats[i].mScore;
			Chunk chunk = ChunkFactory.createChunk(start,end,type,score);
			chunking.add(chunk);
		}
	}

}
