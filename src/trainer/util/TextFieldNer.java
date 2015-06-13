package trainer.util;

import java.util.Vector;

import javax.swing.JTextPane;

import dictionary.approximatedDictionaries.AproximatedDictionary;
import dictionary.chunk.Chunk;
import dictionary.dictionaryentry.DictionaryEntry;
import dictionary.io.DictionaryIO;
import ner.NER;

@SuppressWarnings("serial")
public class TextFieldNer extends JTextPane {
	
	private NER ner;
	private Vector<DictionaryEntry> entries = null;
	
	
	public TextFieldNer(){
		this.initNer();
	}
	
	public void addEntry(String token, Vector<String> categories){
		String[] sCategories = new String[categories.size()];
		categories.toArray(sCategories);
		entries.add(new DictionaryEntry(token,sCategories));
		this.updateEntries();
	}
	
	public void loadDictionary(String path){
		entries.addAll( DictionaryIO.loadPlainTextWithCategories(path) );
		this.updateEntries();
	}
	
	private void updateEntries(){
		( (AproximatedDictionary)this.ner.getDictionaries().elementAt(0) ).setEntriesList(entries);
	}
	
	private void initNer(){
		this.ner = new NER(true);
		this.entries = new Vector<DictionaryEntry>();
		AproximatedDictionary dic3 = new AproximatedDictionary(entries, 1, 2, 1, false);
		this.ner.addDictionary(dic3);
	}
	
	@Override
	public void setText(String t) {
		Vector<Chunk> chunks = this.ner.recognize(t);
		for(Chunk chunk : chunks){
			t = t.replace(chunk.getText(), "<span style=\"color: #11A800; font-weight: bold\">"+chunk.getText()+"</span>");
		}
		super.setText(t);
	}
}
