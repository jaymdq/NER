package configuration;

import java.util.Comparator;
import java.util.Vector;

import syntax.SyntaxChecker;
import dictionary.chunk.Chunk;
import dictionary.chunk.comparator.ChunkComparatorByStart;

public class SyntaxCheckerConfigurator extends AbsConfigurator {

	// Variables

	// Constructors
	
	public SyntaxCheckerConfigurator(String name, Comparator<Chunk> comparator) {
		super(name);
		setParameter("comparator", comparator);
	}
	
	// Getters and Setters

	// Methods

	@Override
	protected int checkParameters(Vector<String> params) {
		//boolean keepLargerChunks, Comparator<Chunk> comparator, int charBetween
		int out = 0;
		
		setParameter("-l",false);
		setParameter("-L",false);
		
		for (int i = 0; i < params.size(); i++){
			switch(params.elementAt(i)){
			case "-l" :
			case "-L" :
				setParameter("-l",true);
				break;
			case "-c":
			case "-C":
				//Check if there is another element next to it
				if (i+1 < params.size()){
					try{
						int intToAdd = Integer.parseInt(params.elementAt(i+1));
						setParameter("-c",intToAdd);
						i++;
					} catch (Exception e) {
						out = 1;
					}
				}else
					out = 2;
				break;			
			default:
				out = 3;
				break;
			}
		}
		
		return out;
	}

	@Override
	protected Object configureObject() {
		SyntaxChecker out ;
		
		boolean keepLargerChunks = (boolean) getParameter("-l");
		@SuppressWarnings("unchecked")
		Comparator<Chunk> comparator = (Comparator<Chunk>) getParameter("comparator");
		int charBetween = (int) getParameter("-c");
		
		if (comparator != null)
			out = new SyntaxChecker(keepLargerChunks,comparator,charBetween);
		else
			out = new SyntaxChecker(keepLargerChunks,new ChunkComparatorByStart(),charBetween);
		
		return out;
	}

	@Override
	public String getErrorReason() {
		String out = "";
		switch (getOperationStatus()){
		case 0 :
			out = "No errors";
			break;
		case 1 :
			out = "-c/-C INTEGER";
			break;
		case 2 :
			out = "We need more arguments";
			break;
		default:
			out = "Invalid argument";
			break;
		}
		
		return out;
	}
}
