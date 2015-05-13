package trainer;

import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import trainer.stream.StreamWorkerAbs;
import trainer.stream.plaintext.PlainTextFormatAbs;
import trainer.stream.plaintext.PlainTextFormatSimple;
import trainer.stream.plaintext.PlainTextFormatTwitter;
import trainer.stream.plaintext.StreamPlainTextWorker;
import trainer.stream.twitter.StreamTwitterWorker;
import trainer.util.CBEntryBox;
import trainer.util.TFPlaceHolder;

import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;

import trainer.util.LBCounter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import dictionary.io.DictionaryIO;

public class TrainerUI {

	private JFrame frmNerTrainer;
	private TFPlaceHolder tfTagsStreaming;
	private CBEntryBox cbEntryMethod;
	private JButton btnSelectFile;
	
	private StreamWorkerAbs streamWorker;
	private JTextField tfTokenToAdd;
	private JButton btnNextTweet;
	private LBCounter lblCounter;
	private JButton btnStartTrainer;
	private JTextField tfToRecognice;
	private JTextField tfTokenResult;
	private JTable tokensTable;
	private JList<String> listCategoriesResult;
	private DefaultListModel<String> listCategoriesResultModel;
	private JList<String> listCategoriesToSelect;
	private DefaultListModel<String> listCategoriesToSelectModel;
	
	private String[] selectedFilePath = new String[]{ null, null};
	private JComboBox<String> cbFormatFile;

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
		
		JPanel mainPanel = new JPanel();
		
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		GroupLayout groupLayout = new GroupLayout(frmNerTrainer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
				.addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
					.addGap(8)
					.addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
		);
		
		lblCounter = new LBCounter("Total tweets: ");
		GroupLayout gl_statusBar = new GroupLayout(statusBar);
		gl_statusBar.setHorizontalGroup(
			gl_statusBar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusBar.createSequentialGroup()
					.addComponent(lblCounter, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(618, Short.MAX_VALUE))
		);
		gl_statusBar.setVerticalGroup(
			gl_statusBar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusBar.createSequentialGroup()
					.addComponent(lblCounter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		statusBar.setLayout(gl_statusBar);
		
		JPanel inputPanel = new JPanel();
		
		JPanel streamPanel = new JPanel();
		
		JPanel tokensPanel = new JPanel();
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(tokensPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
						.addComponent(inputPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
						.addComponent(streamPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(streamPanel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(inputPanel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tokensPanel, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
		);
		
		JScrollPane tablePanel = new JScrollPane();
		GroupLayout gl_tokensPanel = new GroupLayout(tokensPanel);
		gl_tokensPanel.setHorizontalGroup(
			gl_tokensPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
		);
		gl_tokensPanel.setVerticalGroup(
			gl_tokensPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
		);
		
		tokensTable = new JTable();
		tablePanel.setViewportView(tokensTable);
		tokensPanel.setLayout(gl_tokensPanel);
		
		cbEntryMethod = new CBEntryBox();
		
		tfTagsStreaming = new TFPlaceHolder("tag1, tag2, tag3 ...");
		tfTagsStreaming.setColumns(10);
		tfTagsStreaming.setVisible(false);
		
		btnSelectFile = new JButton("Select file");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectFile();
			}
		});
		btnSelectFile.setVisible(true);
		cbFormatFile = new JComboBox<String>();
		cbFormatFile.setModel(new DefaultComboBoxModel<String>(new String[] {"Plain Text", "Twitter Format"}));
		cbFormatFile.setVisible(true);
		
		cbEntryMethod.addEntry("Load from file", new JComponent[] {btnSelectFile, cbFormatFile});
		cbEntryMethod.addEntry("Streaming Twitter", new JComponent[] {tfTagsStreaming});
		
		
		btnStartTrainer = new JButton("Start Trainer");
		btnStartTrainer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startTrainer();
			}
		});
		
		tfToRecognice = new JTextField();
		tfToRecognice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				tfTokenToAdd.setText(tfToRecognice.getSelectedText());
			}
		});
		tfToRecognice.setEditable(false);
		tfToRecognice.setColumns(10);
		
		btnNextTweet = new JButton("Next tweet");
		btnNextTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getNextTweet();
			}
		});
		btnNextTweet.setEnabled(false);
		lblCounter.asignButtonToCounter(btnNextTweet);
		
		GroupLayout gl_streamPanel = new GroupLayout(streamPanel);
		gl_streamPanel.setHorizontalGroup(
			gl_streamPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_streamPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_streamPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_streamPanel.createSequentialGroup()
							.addComponent(tfToRecognice, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNextTweet))
						.addGroup(gl_streamPanel.createSequentialGroup()
							.addComponent(cbEntryMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfTagsStreaming, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbFormatFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSelectFile)
							.addPreferredGap(ComponentPlacement.RELATED, 225, Short.MAX_VALUE)
							.addComponent(btnStartTrainer)))
					.addContainerGap())
		);
		gl_streamPanel.setVerticalGroup(
			gl_streamPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_streamPanel.createSequentialGroup()
					.addGroup(gl_streamPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbEntryMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfTagsStreaming, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSelectFile)
						.addComponent(btnStartTrainer)
						.addComponent(cbFormatFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_streamPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfToRecognice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNextTweet))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		streamPanel.setLayout(gl_streamPanel);
		
		JPanel tokenInsertPanel = new JPanel();
		
		JPanel finalToken = new JPanel();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JLabel lblTokenToAdd = new JLabel("Token to add");
		
		tfTokenResult = new JTextField();
		tfTokenResult.setEnabled(false);
		tfTokenResult.setColumns(10);
		
		JLabel lblCategories = new JLabel("Categories");
		
		JButton btnAddToken = new JButton("Add token");
		btnAddToken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GroupLayout gl_finalToken = new GroupLayout(finalToken);
		gl_finalToken.setHorizontalGroup(
			gl_finalToken.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_finalToken.createSequentialGroup()
					.addGroup(gl_finalToken.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTokenToAdd)
						.addComponent(lblCategories))
					.addGap(18)
					.addGroup(gl_finalToken.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddToken)
						.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
						.addComponent(tfTokenResult, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_finalToken.setVerticalGroup(
			gl_finalToken.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_finalToken.createSequentialGroup()
					.addGroup(gl_finalToken.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTokenToAdd)
						.addComponent(tfTokenResult, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_finalToken.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategories)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddToken)
					.addContainerGap(10, Short.MAX_VALUE))
		);
		
		listCategoriesResult = new JList<String>();
		listCategoriesResultModel = new DefaultListModel<String>();
		listCategoriesResult.setModel(listCategoriesResultModel);
		listCategoriesResult.setEnabled(false);
		scrollPane_1.setViewportView(listCategoriesResult);
		finalToken.setLayout(gl_finalToken);
		GroupLayout gl_inputPanel = new GroupLayout(inputPanel);
		gl_inputPanel.setHorizontalGroup(
			gl_inputPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_inputPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(tokenInsertPanel, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(finalToken, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_inputPanel.setVerticalGroup(
			gl_inputPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_inputPanel.createSequentialGroup()
					.addGroup(gl_inputPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(finalToken, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(tokenInsertPanel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 159, Short.MAX_VALUE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JLabel lblToken = new JLabel("Token");
		
		tfTokenToAdd = new JTextField();
		tfTokenToAdd.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {
					updateTokenResult();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					updateTokenResult();
				}
				@Override
				public void removeUpdate(DocumentEvent arg0) { 
					updateTokenResult();
				}
				
				private void updateTokenResult(){
					tfTokenResult.setText(tfTokenToAdd.getText());
				}
			});
		tfTokenToAdd.setColumns(10);
		
		JLabel lblCategory = new JLabel("Category");
		
		JScrollPane scrollPane = new JScrollPane();
		
		listCategoriesToSelect = new JList<String>();
		listCategoriesToSelect.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				List<String> tmpList = listCategoriesToSelect.getSelectedValuesList();
				listCategoriesResultModel.clear();
				for(String s: tmpList){
					listCategoriesResultModel.addElement(s);
				}
			}
		});
		listCategoriesToSelectModel = new DefaultListModel<String>();
		listCategoriesToSelect.setModel(listCategoriesToSelectModel);
		/*listCategoriesToSelectModel.addElement("Vehiculo");
		listCategoriesToSelectModel.addElement("Persona");
		listCategoriesToSelectModel.addElement("Color");
		listCategoriesToSelectModel.addElement("Calle");
		listCategoriesToSelectModel.addElement("Localidad");*/
		//listCategoriesToSelectModel.copyInto(new String[] {"Vehiculo", "Persona", "Color", "Calle", "Localidad"});
		scrollPane.setViewportView(listCategoriesToSelect);
		
		JButton btnAddCategory = new JButton("Add category");
		btnAddCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String category = JOptionPane.showInputDialog("New category");
				( (DefaultListModel<String>) listCategoriesToSelect.getModel() ).addElement(category);
			}
		});
		GroupLayout gl_tokenInsertPanel = new GroupLayout(tokenInsertPanel);
		gl_tokenInsertPanel.setHorizontalGroup(
			gl_tokenInsertPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tokenInsertPanel.createSequentialGroup()
					.addComponent(lblToken)
					.addGap(30)
					.addComponent(tfTokenToAdd, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
					.addGap(5))
				.addGroup(gl_tokenInsertPanel.createSequentialGroup()
					.addComponent(lblCategory)
					.addGap(18)
					.addGroup(gl_tokenInsertPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddCategory)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_tokenInsertPanel.setVerticalGroup(
			gl_tokenInsertPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tokenInsertPanel.createSequentialGroup()
					.addGroup(gl_tokenInsertPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblToken)
						.addComponent(tfTokenToAdd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tokenInsertPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategory)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddCategory)
					.addContainerGap(10, Short.MAX_VALUE))
		);
		tokenInsertPanel.setLayout(gl_tokenInsertPanel);
		inputPanel.setLayout(gl_inputPanel);
		mainPanel.setLayout(gl_mainPanel);
		frmNerTrainer.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmNerTrainer.setJMenuBar(menuBar);
		
		JMenu mnfile = new JMenu("File");
		mnfile.setMnemonic('F');
		menuBar.add(mnfile);
		
		JMenuItem mntmnew = new JMenuItem("New");
		mnfile.add(mntmnew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openDictionary();
			}
		});
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

	private void selectFile() {
		JFileChooser chooser = new JFileChooser("./");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain Text (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(frmNerTrainer);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFilePath[0] = chooser.getSelectedFile().getPath();
			selectedFilePath[1] = chooser.getSelectedFile().getName();
		}
	}

	private void getNextTweet() {
		String tweet = this.streamWorker.getNextTweet();
		if(tweet != null) this.tfToRecognice.setText(tweet);
	}

	private void startTrainer() {
		switch(cbEntryMethod.getSelectedIndex()){
			case 0:
				if(this.selectedFilePath[0] != null && this.selectedFilePath[1] != null){
					PlainTextFormatAbs format = null;
					switch(this.cbFormatFile.getSelectedIndex()){
						case 0:
							format = new PlainTextFormatSimple();
							break;
						case 1:
							format = new PlainTextFormatTwitter();
							break;
					}
					this.streamWorker = new StreamPlainTextWorker(this.selectedFilePath[0], format);
					this.streamWorker.setCounter(this.lblCounter);
					this.streamWorker.start();
				}
				break;
			case 1:
				this.streamWorker = new StreamTwitterWorker(this.tfTagsStreaming.getText());
				this.streamWorker.setCounter(this.lblCounter);
				this.streamWorker.start();
				break;
		}
		this.btnStartTrainer.setEnabled(false);
	}
	
	private void openDictionary(){
		selectFile();
		Set<String> categories = DictionaryIO.getPlainTextCategories(this.selectedFilePath[0]);
		for(String category: categories){
			listCategoriesToSelectModel.addElement(category);
		}
	}
}
