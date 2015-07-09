package dictionary.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.HashMap;

import org.apache.log4j.Logger;

import dictionary.dictionaryEntry.DictionaryEntry;

public class DictionaryIO {

	// Variables
	
	public static final String DIC_EXT = ".dic";
	public static final String EMPTY_EXT = "";
	public static final String CATEGORY_INDICATOR = "#CATEGORY";
	public static final String CATEGORY_SEPARATOR = ",";

	// Constructors
	
	// Getters and Setters
	
	// Methods
	
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

	public static boolean savePlainTextWithCategories(String path, HashMap<String, Vector<String>> dictionary){
		if(dictionary == null)
			return false;
		
		Set<String> categories = dictionary.keySet();
		
		boolean outputBoolean = false;
		
		Writer out = null;
		boolean firstLine = true;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
			for(String category: categories){
				if(!firstLine) out.write("\r\n"); else firstLine = false;
				out.write(CATEGORY_INDICATOR+" "+category+"\r\n");
				out.write("\r\n");
				for(String token: dictionary.get(category))
					out.write(token+"\r\n");
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
		    try {
				out.close();
				outputBoolean = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return outputBoolean;
	}
	
}
