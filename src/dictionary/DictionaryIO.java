package dictionary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import twitter4j.Logger;



public class DictionaryIO {

	//Public Extensions
	public static final String DIC_EXT = ".dic";
	public static final String EMPTY_EXT = "";

	//TODO vale la pena hacer esta clase Singleton?

	public static void saveToFile(String path, String extension, Vector<DictionaryEntry> entries){
		ObjectOutputStream outFile = null;
		try {
			outFile = new ObjectOutputStream(new FileOutputStream(path + extension));
			outFile.writeObject(entries);
			outFile.close();
		} catch (IOException e) {
			Logger.getLogger(DictionaryIO.class).error("IO Error While Saving");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Vector<DictionaryEntry> loadFromFile(String path){
		Vector<DictionaryEntry> out = null;
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path));
			out = (Vector<DictionaryEntry>) inFile.readObject();
			inFile.close();
		} catch (IOException | ClassNotFoundException e) {
			Logger.getLogger(DictionaryIO.class).error("IO Error While Loading");
			e.printStackTrace();
		}
		return out;
	}



}
