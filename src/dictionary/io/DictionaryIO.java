package dictionary.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import dictionary.DictionaryEntry;
import twitter4j.Logger;



public class DictionaryIO {

	//Public Extensions
	public static final String DIC_EXT = ".dic";
	public static final String EMPTY_EXT = "";
	public static final String CATEGORY_INDICATOR = "#CATEGORY";
	public static final String CATEGORY_SEPARATOR = ",";

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

	public static Collection<DictionaryEntry> loadPlainTextWithCategories(String path) {
		Vector<DictionaryEntry> out = new Vector<DictionaryEntry>();
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			file = new File (path);
			fr = new FileReader (file);
			br = new BufferedReader(fr);

			String line;
			Vector<String> categories = new Vector<String>();
			while( (line=br.readLine()) != null ){
				
				//New Categories
				if (line.startsWith(CATEGORY_INDICATOR)){
					categories.clear();
					line = line.split(CATEGORY_INDICATOR)[1];
					for (String category : line.split(CATEGORY_SEPARATOR)){
						categories.add(category.trim());
					}
				}else{

					//New Entry
					line = line.trim();
					if (!line.isEmpty()){
						DictionaryEntry toAdd = new DictionaryEntry(line, categories.toArray(new String[categories.size()]) );
						out.add(toAdd);
					}
				}

			}

		}
		catch(Exception e){
			Logger.getLogger(DictionaryIO.class).error("IO Error While Loading");
			e.printStackTrace();
		}finally{
			try{                   
				if( null != fr ){  
					fr.close();    
				}                 
			}catch (Exception e2){
				Logger.getLogger(DictionaryIO.class).error("IO Error While Closing File");
				e2.printStackTrace();
			}
		}


		return out;
	}

	public static Set<String> getPlainTextCategories(String path) {
		Set<String> out = new HashSet<String>();
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			file = new File (path);
			fr = new FileReader (file);
			br = new BufferedReader(fr);

			String line;
			while( (line=br.readLine()) != null ){
				
				//New Categories
				if (line.startsWith(CATEGORY_INDICATOR)){
					line = line.split(CATEGORY_INDICATOR)[1];
					for (String category : line.split(CATEGORY_SEPARATOR)){
						out.add(category.trim());
					}
				}

			}

		}
		catch(Exception e){
			Logger.getLogger(DictionaryIO.class).error("IO Error While Loading");
			e.printStackTrace();
		}finally{
			try{                   
				if( null != fr ){  
					fr.close();    
				}                 
			}catch (Exception e2){
				Logger.getLogger(DictionaryIO.class).error("IO Error While Closing File");
				e2.printStackTrace();
			}
		}
		return out;
	}

}
