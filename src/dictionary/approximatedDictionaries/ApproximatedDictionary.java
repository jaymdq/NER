package dictionary.approximatedDictionaries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;





import dictionary.Chunk;
import dictionary.Dictionary;
import dictionary.DictionaryEntry;
import segmentation.Segmenter;


public class ApproximatedDictionary implements Dictionary {

	private double distanceThreshold;	//TODO serviria para devolver las distancias minimas
	
	public ApproximatedDictionary(Vector<DictionaryEntry> entries, double distanceThreshold){
		setDistanceThreshold(distanceThreshold);
		
		//Leer todas las entradas y crear el diccionario
	}
	
	
	@Override
	public Vector<Chunk> recognize(String text, boolean debugMode) {
		Vector<Chunk> out = new Vector<Chunk>();


		int length = text.length();

		//TODO chequear que el segmentador este bien configurado
		Segmenter segmenter = new Segmenter(text, false ,true);


		boolean[] startTokens = new boolean[length];
		boolean[] endTokens = new boolean[length+1];
		Arrays.fill(startTokens,false);
		Arrays.fill(endTokens,false);
		String token;

		while ((token = segmenter.getNextToken()) != null) {
			int lastStart = segmenter.getLastTokenStartPosition();
			startTokens[lastStart] = true;
			endTokens[lastStart + token.length()] = true;
		}
/*
		Map<Dp,Chunk> dpToChunk = new HashMap<Dp,Chunk>();
		Map<SearchState,SearchState> queue = new HashMap<SearchState,SearchState>();
		
		for (int i = 0; i < length; ++i) {
			int startPlusI = i;
			char c = cs[startPlusI];
			if (startTokens[i]) {
				add(queue,mDictionary.mRootNode,startPlusI,
						0.0,
						false,dpToChunk,cs,startPlusI);
			}
			Map<SearchState,SearchState> nextQueue = new HashMap<SearchState,SearchState>();
			double deleteCost = -mEditDistance.deleteWeight(c);
			for (SearchState state : queue.values()) {

				// delete
				add(nextQueue,state.mNode,state.mStartIndex,
						state.mScore + deleteCost,
						endTokens[i+1],dpToChunk,cs,startPlusI);

				// match or subst
				char[] dtrChars = state.mNode.mDtrChars;
				Node<String>[] dtrNodes = state.mNode.mDtrNodes;
				for (int j = 0; j < dtrChars.length; ++j) {
					add(nextQueue,dtrNodes[j],state.mStartIndex,
							state.mScore
							- (dtrChars[j] == c
							?  mEditDistance.matchWeight(dtrChars[j])
									: mEditDistance.substituteWeight(dtrChars[j],c)),
									endTokens[i+1],dpToChunk,cs,startPlusI);
				}

			}
			queue = nextQueue;
		}


*/
		return out;
	}


	public double getDistanceThreshold() {
		return distanceThreshold;
	}


	public void setDistanceThreshold(double distanceThreshold) {
		this.distanceThreshold = distanceThreshold;
	}

}
