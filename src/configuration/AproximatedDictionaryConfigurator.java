package configuration;

import java.util.Vector;

import dictionary.approximatedDictionaries.AproximatedDictionary;
import dictionary.dictionaryentry.DictionaryEntry;

public class AproximatedDictionaryConfigurator extends AbsDictionaryConfigurator {

	// Variables

	// Constructors

	public AproximatedDictionaryConfigurator(String name, Vector<DictionaryEntry> entries) {
		super(name, entries);
	}

	// Getters and Setters

	// Methods

	@Override
	protected int checkParameters(Vector<String> params) {
		//Vector<DictionaryEntry> entriesList, double lowerLimit, int n_gram, int threshold, boolean caseSensitive
		int out = 0;
		
		setParameter("-c",false);
		
		for (int i = 0; i < params.size(); i++){
			switch(params.elementAt(i)){
			case "-c" :
			case "-C" :
				setParameter("-c",true);
				break;
			case "-l":
			case "-L":
				//Check if there is another element next to it
				if (i+1 < params.size()){
					try{
						double doubleToAdd = Double.parseDouble(params.elementAt(i+1));
						setParameter("-l",doubleToAdd);
						i++;
					} catch (Exception e) {
						out = 1;
					}
				}else
					out = 4;
				break;
			case "-n":
			case "-N":
				//Check if there is another element next to it
				if (i+1 < params.size()){
					try{
						int intToAdd = Integer.parseInt(params.elementAt(i+1));
						setParameter("-n",intToAdd);
						i++;
					} catch (Exception e) {
						out = 2;
					}
				}else
					out = 4;
				break;
			case "-t":
			case "-T":
				//Check if there is another element next to it
				if (i+1 < params.size()){
					try{
						int intToAdd = Integer.parseInt(params.elementAt(i+1));
						setParameter("-t",intToAdd);
						i++;
					} catch (Exception e) {
						out = 3;
					}
				}else
					out = 4;
				break;
			
			default:
				out = 5;
				break;
			}
		}

		return out;
	}

	@Override
	protected Object configureObject() {
		AproximatedDictionary out = null;

		//Parameters
		boolean caseSensitive = (boolean) getParameter("-c");
		double lowerLimit = (double) getParameter("-l");
		int n_gram = (int) getParameter("-n");
		int threshold = (int) getParameter("-t");
		
		out = new AproximatedDictionary(getEntries(),lowerLimit,n_gram,threshold,caseSensitive);
				
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
			out = "-l/-L DOUBLE";
			break;
		case 2 :
			out = "-n/-N INTEGER";
			break;
		case 3 :
			out = "-n/-N INTEGER";
			break;
		case 4 :
			out = "We need another argument";
			break;
		default:
			out = "Invalid argument";
			break;
		}
		
		return out;
	}

}
