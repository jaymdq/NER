package entry;
import java.util.Vector;


public class TextEntry extends AbsEntry {

	// Variables
	
	//Constructors
	
	private TextEntry (){
		this.statusList  = new Vector<String>();
	}
	
	// Getters And Setters
	
	/**
	 * 
	 * @return AbsEntry con la instancia.
	 */
	public static AbsEntry getInstance() {
		if (instance == null){
			instance = new TextEntry();
		}
		return instance;
	}
	
	// Methods
	
	@SuppressWarnings("unchecked")
	@Override
	public String getTextFromStatus() {
		String text = null;
		try {
			if ( ( text = this.entryBuffer.readLine() ) != null)   {
				this.statusList.add(text);
			}else
				entryData.close();
		} catch (Exception e) {
			log.error("Error leyendo el archivo de origen");
		}		
		return text;
	}

}
