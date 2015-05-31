package syntax;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import score.Score;
import utils.Pair;
import dictionary.Chunk;
import dictionary.ChunkComparator;

public class SyntaxChecker {

	/*
	 * La idea seria talvez tener como una estructura como Trie (árbol) y tener reglas en cada nodo
	 * de tal forma que si encontras -> calle -> preposicion (y) -> calle = Interseección
	 * La idea tmb seria devolver nuevamente la lista de Chunks con los cambios realizados
	 * Las reglas se deben tener que guardar tmb exteriormente. 
	 * Ademas esto se tendria que hacer con un grado de certeza, osea si tenes "Alen y Montevideo", Montevideo esta bien escrita
	 * pero Alem No, osea existe una incerteza que el mismo chunck "Alen" ya conoce (distancia 1).
	 * Al convertir a un nuevo Chunk tenemos que seguir manteniendo esa certeza.
	 */

	private boolean keepLargerChunks;
	private Comparator<Chunk> comparator;
	private AbsSyntaxTrieNode root;
	private int charBetween = 3;

	public SyntaxChecker(){
		this(true,new ChunkComparator());
	}

	public SyntaxChecker(boolean keepLargerChunks){
		this(keepLargerChunks, new ChunkComparator());
	}

	public SyntaxChecker(Comparator<Chunk> comparator){
		this(true, comparator);
	}

	public SyntaxChecker(boolean keepLargerChunks, Comparator<Chunk> comparator){
		this.keepLargerChunks = keepLargerChunks;
		this.comparator = comparator;
		root = new SyntaxTrieNodeInter(null);
	}

	public void addRules(Vector<Pair<Vector<String>,String>> rules){
		for ( Pair<Vector<String>,String> pair : rules){
			root.addToMap(pair.getPair1(), pair.getPair2());
		}
	}

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
					
			}
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
				categoriesOut.add(chunk.type());
				if (outClear.lastElement() != chunk){
					i = posOut.firstElement()+1;
					while(i < outClear.size() && chunk.start() == outClear.get(i).start()){
						if(chunk.end() == outClear.get(i).end()){
							posOut.add(i);
							categoriesOut.add(outClear.get(i).type());
						}
						i++;
					}
				}
				chunksByCategory.add(new Pair<Vector<Integer>, Vector<String>>(posOut, categoriesOut));
			}
		}
		
		Vector<String> possibleJoin = null;
		Vector< Pair< Vector<Integer>, Vector<String>> > categoriesOfChunks = this.getCategoriesOfChunks( (Vector< Pair< Vector<Integer>, Vector<String>> >) chunksByCategory.clone());
		
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
								( chunk1.getText().equals(chunk2.getText()) && chunk1.type().equals(chunk2.type()) ) ){
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
	
	private Vector< Pair< Vector<Integer>, Vector<String>> > getCategoriesOfChunks( Vector< Pair< Vector<Integer>, Vector<String>> > chunksByCategories){
		if(chunksByCategories.isEmpty())
			return null;
		
		Pair<Vector<Integer>, Vector<String>> chunk = chunksByCategories.remove(0);
		
		Vector< Pair< Vector<Integer>, Vector<String>> > tmp = getCategoriesOfChunks(chunksByCategories);
		
		if( tmp != null ){
			int i=0;
			for(String s: chunk.getPair2()) {
				for(Pair< Vector<Integer>, Vector<String>> vec: tmp){
					vec.getPair2().add(0, s);
					vec.getPair1().add(0, chunk.getPair1().get(i));
				}
				i++;
			}
		
		}else{
			
			tmp = new Vector< Pair< Vector<Integer>, Vector<String>> >();
			int i=0;
			for(String s: chunk.getPair2()) {
				Vector<Integer> intList = new Vector<Integer>();
				Vector<String> stringList = new Vector<String>();
				intList.add( chunk.getPair1().get(i));
				stringList.add(s);
				tmp.add( new Pair< Vector<Integer>, Vector<String>>(intList, stringList) );
				i++;
			}
			
		}
		
		return tmp;
	}

}
