package trainer.util;

import java.awt.Label;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class LBCounter extends Label {
	private String baseText;
	private JButton button = null;
	
	public LBCounter(String baseText){
		super(baseText+"0");
		this.baseText = baseText;
	}
	
	public void asignButtonToCounter(JButton button){
		this.button = button;
	}
	
	public void updateCounter(int quantity){
		this.setText(this.baseText+quantity);
		if(button != null) this.refreshButton(quantity);
	}
	
	private void refreshButton(int q){
		this.button.setEnabled( (q > 0) ? true : false );
	}
}
