package preprocess;

import java.util.Vector;

import utils.Pair;

public class PreProcess {

	private Vector<Pair<String, String>> rules;

	public PreProcess(){
		this.setRules(new Vector<Pair<String, String>>());
	}
	
	public PreProcess(Vector<Pair<String, String>> rules){
		this.setRules(rules);
	}

	public Vector<Pair<String, String>> getRules() {
		return rules;
	}

	public void setRules(Vector<Pair<String, String>> rules) {
		this.rules = rules;
	}

	public void addRule(Pair<String, String> rule){
		this.rules.add(rule);
	}

	public String execute(String text){
		String out = text;
		for (Pair<String, String> rule : rules){
			out = findAndReplace(out,rule);
		}
		
		return out;
	}

	private String findAndReplace(String text, Pair<String, String> rule){
		String out = text;
		out = out.replaceAll(rule.getPair1(), rule.getPair2());
		return out;
	}

}
