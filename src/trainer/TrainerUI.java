package trainer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;
import javax.swing.JPanel;

public class TrainerUI {

	private JFrame frmNerTrainer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainerUI window = new TrainerUI();
					window.frmNerTrainer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrainerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNerTrainer = new JFrame();
		frmNerTrainer.setTitle("NER Trainer");
		frmNerTrainer.setBounds(100, 100, 800, 600);
		frmNerTrainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GroupLayout groupLayout = new GroupLayout(frmNerTrainer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 784, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 539, Short.MAX_VALUE)
		);
		frmNerTrainer.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmNerTrainer.setJMenuBar(menuBar);
		
		JMenu mnfile = new JMenu("File");
		mnfile.setMnemonic('F');
		menuBar.add(mnfile);
		
		JMenuItem mntmnew = new JMenuItem("New");
		mnfile.add(mntmnew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnfile.add(mntmOpen);
		
		JSeparator separator = new JSeparator();
		mnfile.add(separator);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnfile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save as");
		mnfile.add(mntmSaveAs);
		
		JSeparator separator_1 = new JSeparator();
		mnfile.add(separator_1);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnfile.add(mntmClose);
	}
}
