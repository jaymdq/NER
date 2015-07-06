package dictionary.approximatedDictionaries;

import java.util.Vector;

import dictionary.dictionaryEntry.DictionaryEntry;

public abstract class AbsTrieNode {
	
	// Variables
	protected Character character;
	
	// Constructors
	/**
	 * Contructor de la clase abstracta de los nodos del arbol trie
	 * @param character El parametro character define el caracter directamente relacionado con el nodo.
	 */
	
	public AbsTrieNode(Character character) {
		super();
		this.character = character;
	}
	
	// Getters And Setters
	/**
	 * 
	 * @return El caracter directamente relacionado con el nodo.
	 */
	
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * 
	 * @param character El parametro character define el caracter directamente relacionado con el nodo.
	 */
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	//Methods
	public abstract Vector<DictionaryEntry> getListOfDictionaryEntries(String text);
	public abstract void addToMap(String text, DictionaryEntry dictionaryEntry);
	
}
