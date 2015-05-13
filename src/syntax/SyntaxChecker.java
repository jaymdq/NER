package syntax;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

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

	public Vector<Chunk> joinChunks(Vector<Chunk> chunks){
		Vector<Chunk> out;

		//Sorting
		out = sortChunks(chunks);

		//Keep Larger Chunks ?
		if (keepLargerChunks)
			out = keepLargerChunks(out);

		//Join Chunks

		Vector< Pair< Vector<Integer>, Vector<String>> > chunksByCategory = new Vector< Pair< Vector<Integer>, Vector<String>> >();
		int i;
		for(Chunk chunk : out){
			Vector<Integer> posOut = new Vector<Integer>();
			Vector<String> categoriesOut = new Vector<String>();
			posOut.add(out.indexOf(chunk));
			categoriesOut.add(chunk.type());
			if (out.lastElement() != chunk){
				i = posOut.firstElement()+1;
				while(chunk.start() == out.get(i).start()){
					if(chunk.end() == out.get(i).end()){
						posOut.add(i);
						categoriesOut.add(out.get(i).type());
					}
					i++;
				}
			}
			chunksByCategory.add(new Pair<Vector<Integer>, Vector<String>>(posOut, categoriesOut));
		}
		
		
		//TODO MAX!!!!!!!!!
		Vector<String> possibleJoin = null;
		for (Pair< Vector<Integer>, Vector<String>> pair : chunksByCategory){
			//possibleJoin = 
		}
		
		
		/*Vector<String> possibleJoin = null;
		Vector<String> categoriesOfChunks = new Vector<String>();
		for (Chunk chunk : out){
			categoriesOfChunks.add(chunk.type());
		}

		for ( i = 0; i < out.size(); i++){
			for (int j = i + 1 ; j <= out.size(); j++){

				possibleJoin = new Vector<String>(categoriesOfChunks.subList(i,j));

				Vector<String> results = root.getListOfCategories(possibleJoin);

				if (results != null){
					//
					out.add(new Chunk());
				}

			}
		}*/

		return out;
	}

	private Vector<Chunk> keepLargerChunks(Vector<Chunk> chunks){
		@SuppressWarnings("unchecked")
		Vector<Chunk> out = (Vector<Chunk>) chunks.clone();

		for (Chunk chunk1 : chunks){
			for (Chunk chunk2 : chunks){

				if ( ! chunk1.equals(chunk2) ){
					if  ( chunk1.start() >= chunk2.start() && chunk1.end() <= chunk2.end() ) {
						if ( out.contains(chunk1) )
							out.remove(chunk1);
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

}
