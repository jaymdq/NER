package syntax;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import utils.Pair;
import dictionary.chunk.Chunk;
import dictionary.chunk.comparator.ChunkComparatorByStart;
import dictionary.chunk.score.Score;

public class SyntaxChecker {

	// Variables

	private boolean keepLargerChunks;
	private Comparator<Chunk> comparator;
	private AbsSyntaxTrieNode root;
	private int charBetween;

	// Constructors

	public SyntaxChecker(){
		this(true,new ChunkComparatorByStart(), 3);
	}

	public SyntaxChecker(boolean keepLargerChunks){
		this(keepLargerChunks, new ChunkComparatorByStart(), 3);
	}

	public SyntaxChecker(Comparator<Chunk> comparator){
		this(true, comparator, 3);
	}

	public SyntaxChecker(boolean keepLargerChunks, Comparator<Chunk> comparator, int charBetween){
		this.keepLargerChunks = keepLargerChunks;
		this.comparator = comparator;
		this.charBetween = charBetween;
		root = new SyntaxTrieNodeInter(null);
	}

	// Getters And Setters

	public boolean isKeepLargerChunks() {
		return keepLargerChunks;
	}

	public void setKeepLargerChunks(boolean keepLargerChunks) {
		this.keepLargerChunks = keepLargerChunks;
	}

	public Comparator<Chunk> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<Chunk> comparator) {
		this.comparator = comparator;
	}

	public int getCharBetween() {
		return charBetween;
	}

	public void setCharBetween(int charBetween) {
		this.charBetween = charBetween;
	}

	
	// Methods

	public void addRules(Vector<Pair<Vector<String>,String>> rules){
		for ( Pair<Vector<String>,String> pair : rules){
			root.addToMap(pair.getPair1(), pair.getPair2());
		}
	}

	@SuppressWarnings("unchecked")
	public Vector<Chunk> joinChunks(Vector<Chunk> chunks, String text){
		Vector<Chunk> out;

		//Sorting
		out = sortChunks(chunks);

		//Keep Larger Chunks ?
		if (keepLargerChunks)
			out = keepLargerChunks(out);

		// Check distance to remove chunks
		int i;
		HashSet<Integer> chunksPos = new HashSet<Integer>();
		for( Chunk chunk : out ){
			if(out.lastElement() != chunk){
				i = out.indexOf(chunk)+1;
				if( ( out.get(i).start()-chunk.end() ) <= this.charBetween ) {
					chunksPos.add(out.indexOf(chunk));
					chunksPos.add(i);
				}

			}else
				chunksPos.add(out.indexOf(chunk));

		}
		Vector<Chunk> outClear = new Vector<Chunk>();
		for(Integer data: chunksPos)
			outClear.add(out.get(data));

		//Join Chunks

		Vector< Pair< Vector<Integer>, Vector<String>> > chunksByCategory = new Vector< Pair< Vector<Integer>, Vector<String>> >();

		for(Chunk chunk : outClear){
			if(	( chunksByCategory.size() <= 0 )  || 
					( ! chunksByCategory.lastElement().getPair1().contains( outClear.indexOf(chunk) ) ) ) {
				Vector<Integer> posOut = new Vector<Integer>();
				Vector<String> categoriesOut = new Vector<String>();
				posOut.add(outClear.indexOf(chunk));
				categoriesOut.add(chunk.getCategoryType());
				if (outClear.lastElement() != chunk){
					i = posOut.firstElement()+1;
					while(i < outClear.size() && chunk.start() == outClear.get(i).start()){
						if(chunk.end() == outClear.get(i).end()){
							posOut.add(i);
							categoriesOut.add(outClear.get(i).getCategoryType());
						}
						i++;
					}
				}
				chunksByCategory.add(new Pair<Vector<Integer>, Vector<String>>(posOut, categoriesOut));
			}
		}

		Vector<String> possibleJoin = null;
		Vector< Pair< Vector<Integer>, Vector<String>> > categoriesOfChunks = this.getCategoriesOfChunks( (Vector< Pair< Vector<Integer>, Vector<String>> >) chunksByCategory.clone());

		if (categoriesOfChunks == null)
			return out;
		
		for( int cat = 0; cat < categoriesOfChunks.size(); cat++ ){
			Pair< Vector<Integer>, Vector<String>> categoryOfChunks = categoriesOfChunks.get(cat);
			for ( i = 0; i < categoryOfChunks.getPair2().size(); i++){
				for (int j = i + 1 ; j <= categoryOfChunks.getPair2().size(); j++){

					possibleJoin = new Vector<String>(categoryOfChunks.getPair2().subList(i,j));
					Vector<String> results = root.getListOfCategories(possibleJoin);
					if (results != null){
						Vector<Chunk> chunksTmp = new Vector<Chunk>();
						for(Integer chunkPos: ( categoryOfChunks.getPair1() ).subList(i, j)){
							chunksTmp.add( outClear.get(chunkPos) );
						}
						for(String result: results)
							out.add(new Chunk(chunksTmp.firstElement().start(), chunksTmp.lastElement().end(), result, text, Score.getInstance().getScoreFromChunks(chunksTmp) ));
					}

				}
			}
		}

		if (keepLargerChunks)
			out = keepLargerChunks(out);

		return out;
	}

	private Vector<Chunk> keepLargerChunks(Vector<Chunk> chunks){
		@SuppressWarnings("unchecked")
		Vector<Chunk> out = (Vector<Chunk>) chunks.clone();

		for (Chunk chunk1 : chunks){
			for (Chunk chunk2 : chunks){
				if( out.contains(chunk2) ){
					if ( ! chunk1.equals(chunk2) ){
						if  ( chunk1.start() >= chunk2.start() && chunk1.end() <= chunk2.end() ) {
							if( ! chunk1.getText().equals(chunk2.getText()) ||
									( chunk1.getText().equals(chunk2.getText()) && chunk1.getCategoryType().equals(chunk2.getCategoryType()) ) ){
								if ( out.contains(chunk1) )
									out.remove(chunk1);
							}
						}
					}
				}
			}
		}

		return out;
	}

	private Vector<Chunk> sortChunks(Vector<Chunk> chunks){
		@SuppressWarnings("unchecked")
		Vector<Chunk> out = (Vector<Chunk>) chunks.clone();

		if (comparator != null)
			Collections.sort(out, comparator);

		return out;
	}

	@SuppressWarnings("unchecked")
	private Vector< Pair< Vector<Integer>, Vector<String>> > getCategoriesOfChunks( Vector< Pair< Vector<Integer>, Vector<String>> > chunksByCategories){
		if(chunksByCategories.isEmpty())
			return null;

		Pair<Vector<Integer>, Vector<String>> chunk = chunksByCategories.remove(0);

		Vector< Pair< Vector<Integer>, Vector<String>> > tmp = getCategoriesOfChunks(chunksByCategories);


		if( tmp != null ){
			Vector< Pair< Vector<Integer>, Vector<String>> > newTmp = new Vector< Pair< Vector<Integer>, Vector<String>> >();
			int i=0;
			for(String category: chunk.getPair2()) {
				for(Pair< Vector<Integer>, Vector<String>> vec: tmp){
					Vector<Integer> intList = (Vector<Integer>) vec.getPair1().clone();
					Vector<String> stringList = (Vector<String>) vec.getPair2().clone();
					intList.add(0, chunk.getPair1().get(i));
					stringList.add(0, category);
					newTmp.add( new Pair< Vector<Integer>, Vector<String>>(intList, stringList) );
				}
				i++;
			}
			tmp = newTmp;
		}else{

			tmp = new Vector< Pair< Vector<Integer>, Vector<String>> >();
			int i=0;
			for(String category: chunk.getPair2()) {
				Vector<Integer> intList = new Vector<Integer>();
				Vector<String> stringList = new Vector<String>();
				intList.add( chunk.getPair1().get(i));
				stringList.add(category);
				tmp.add( new Pair< Vector<Integer>, Vector<String>>(intList, stringList) );
				i++;
			}

		}

		return tmp;
	}

	public static Pair<Vector<String>,String> createRule(String[] ruleSpec, String result){
		Pair<Vector<String>,String> out = new Pair<Vector<String>, String>();
		out.setPair1(new Vector<String>(Arrays.asList(ruleSpec)));
		out.setPair2(result);
		return out;
	}

	public static Vector<Pair<Vector<String>,String>> createRules(String[] ruleSpec, String result ,Vector<Vector<String>> synonyms){
		Vector<Pair<Vector<String>,String>> rules = new Vector<Pair<Vector<String>,String>>();

		for (int i = 0; i < ruleSpec.length; i++){

			for (Vector<String> synonymsSet : synonyms){
				for (String synonym : synonymsSet){
					if (ruleSpec[i].equals(synonym)){
						for (String synonymToExpand : synonymsSet){
							String[] aux = ruleSpec.clone();
							aux[i] = synonymToExpand;
							rules.add(createRule(aux,result));
						}
					}
				}
			}
		}

		return rules;
	}
}
