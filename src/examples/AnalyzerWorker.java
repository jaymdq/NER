package examples;

import java.util.Collections;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import ner.NER;
import dictionary.chunk.Chunk;
import dictionary.chunk.comparator.ChunkComparatorByStart;

public class AnalyzerWorker extends Thread {

	// Variables

	private int actualTweetCount;
	private Vector<String> tweets;
	private NER ner;
	private JTree tree;
	private Vector<Vector<String>> hashtags;
	private volatile boolean running = true;

	// Constructors
	public AnalyzerWorker(Vector<String> tweets, NER ner, JTree tree, Vector<Vector<String>> hashtags){
		setActualTweetCount(0);
		setTweets(tweets);
		setTree(tree);
		setNer(ner);
		setHashtags(hashtags);
	}

	// Getters and Setters

	public int getActualTweetCount() {
		return actualTweetCount;
	}

	public void setActualTweetCount(int actualTweetCount) {
		this.actualTweetCount = actualTweetCount;
	}

	public Vector<String> getTweets() {
		return tweets;
	}

	public void setTweets(Vector<String> tweets) {
		this.tweets = tweets;
	}

	public NER getNer() {
		return ner;
	}

	public void setNer(NER ner) {
		this.ner = ner;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public Vector<Vector<String>> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Vector<Vector<String>> hashtags) {
		this.hashtags = hashtags;
	}
	
	// Methods

	private Vector<Chunk> sortChunks(Vector<Chunk> chunks){
		@SuppressWarnings("unchecked")
		Vector<Chunk> out = (Vector<Chunk>) chunks.clone();


		Collections.sort(out, new ChunkComparatorByStart());

		return out;
	}

	private Vector<Chunk> removeSamePosChunks(Vector<Chunk> chunks){
		@SuppressWarnings("unchecked")
		Vector<Chunk> out = new Vector<Chunk>();

		if (chunks.size() > 1){
			for (int i = 0; i < chunks.size() - 1; i++){
				if (chunks.elementAt(i).start() != chunks.elementAt(i+1).start() && chunks.elementAt(i).end() != chunks.elementAt(i+1).end()){
					out.add(chunks.elementAt(i));
				}
			}
			int i = chunks.size()-1;
			if (chunks.elementAt(i-1).start() != chunks.elementAt(i).start() && chunks.elementAt(i-1).end() != chunks.elementAt(i).end())
				out.add(chunks.elementAt(i));

		}	
		return out;
	}
	
	public void stopRunning()
	{
	    running = false;
	}

	@Override
	public void run() {
		
		running = true;
		TweetDefaultMutableTreeNode root = new TweetDefaultMutableTreeNode("Tweets [0" + "/" + this.tweets.size() + "]");
		DefaultTreeModel model = new DefaultTreeModel(root);
		tree.setModel(model);


		for (int i = 0; i < tweets.size() && running; i++){
			actualTweetCount++; 

			String tweet = tweets.elementAt(i);
			System.out.println(tweet);
			Vector<Chunk> chunks = this.ner.recognize(tweet);
			String preProcessedTweet = this.ner.getLastPreProcessedString();

			chunks = sortChunks(chunks);
			chunks = removeSamePosChunks(chunks);

			Collections.reverse(chunks);

			StringBuilder auxTweet = new StringBuilder(preProcessedTweet);
			for(Chunk chunk : chunks){	
				auxTweet.insert(chunk.end(), "</span>");	
				auxTweet.insert(chunk.start(), "<span style=\"color:red\">");
			}

			TweetDefaultMutableTreeNode node = new TweetDefaultMutableTreeNode( "<span style=\"color:blue\"><b>[" + (i + 1) + "]:</b></span> [" + auxTweet.toString() + "]");
			root.add(node);

			TweetDefaultMutableTreeNode originalTweet = new TweetDefaultMutableTreeNode("Original Tweet: ["+ tweet +"] - ["+ tweet.length() +"]");
			node.add(originalTweet);

			TweetDefaultMutableTreeNode preProcessedNode = new TweetDefaultMutableTreeNode("Pre-Processed Tweet: ["+ preProcessedTweet +"] - [" + preProcessedTweet.length() + "]");
			node.add(preProcessedNode);

			TweetDefaultMutableTreeNode chunksNode = new TweetDefaultMutableTreeNode("Named Entities: ["+chunks.size()+"]");
			node.add(chunksNode);

			Collections.reverse(chunks);
			for (Chunk chunk : chunks){
				TweetDefaultMutableTreeNode newNode = new TweetDefaultMutableTreeNode(chunk.toString());
				chunksNode.add(newNode);
			}

			TweetDefaultMutableTreeNode hashTagsNode = new TweetDefaultMutableTreeNode("HashTags: ["+hashtags.elementAt(i).size()+"]");
			node.add(hashTagsNode);
			
			for (String ht : hashtags.elementAt(i)){
				TweetDefaultMutableTreeNode newNode = new TweetDefaultMutableTreeNode(ht);
				hashTagsNode.add(newNode);
			}
			
			root.setText("Tweets [" + actualTweetCount + "/" + this.tweets.size() + "]");

			//Reload
			model.reload();
		}
		
		//TODO ver por que si se interrumpe el thread se muestra mal el arbol
		model.reload();
	}

}
