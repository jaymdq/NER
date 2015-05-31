package trainer.util;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TTokensModel extends DefaultTableModel {

	public TTokensModel(){
		super(null, new String[]{
			"Token",
			"Categories"
		});
	}
	
	public void addToken(String token, Vector<String> categories){
		int index = this.indexOf(token);
		if(index < 0){
			Object[] row = new Object[2];
			row[0] = token;
			row[1] = categories;
			this.addRow(row);
		}else{
			updateCategories(index, categories);
		}
		
	}
	
	public int indexOf(String token){
		int index = -1;
		for(int i=0; index < 0 && i < this.getRowCount(); i++){
			if( ( (String)this.getValueAt(i, 0) ).equalsIgnoreCase(token) ){
				index = i;
			}
		}
		return index;
	}
	
	private void updateCategories(int index, Vector<String> categories){
		Vector<String> tmp = (Vector<String>)this.getValueAt(index, 1);
		for(String category: categories)
			if(!tmp.contains(category))
				tmp.add(category);
		this.setValueAt(tmp, index, 1);
	}
	
}
