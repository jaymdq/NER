package configuration;

import java.util.Vector;

import preprocess.PreProcess;
import utils.Pair;

public class PreProcessConfigurator extends AbsConfigurator {

	// Variables

	// Constructors
	
	public PreProcessConfigurator(String name) {
		super(name);
	}
	
	public PreProcessConfigurator(String name, Vector<Pair<String, String>> rules) {
		super(name);
		setParameter("rules",rules);
	}
	
	// Getters and Setters

	// Methods

	@Override
	protected int checkParameters(Vector<String> params) {
					
		return 0;
	}

	@Override
	protected Object configureObject() {
		@SuppressWarnings("unchecked")
		Vector<Pair<String, String>> rules = (Vector<Pair<String, String>>) getParameter("rules");
		PreProcess out;
		if (rules != null)
			out = new PreProcess(rules);
		else
			out = new PreProcess();
			
		return out;
	}
	
	@Override
	public String getErrorReason() {
		return "No errors";
	}
	
}
