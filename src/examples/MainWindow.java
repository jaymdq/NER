package examples;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;

import ner.NER;
import preprocess.PreProcess;
import segmentation.Segmenter;
import syntax.SyntaxChecker;
import trainer.stream.StreamWorkerAbs;
import trainer.stream.plaintext.PlainTextFormatAbs;
import trainer.stream.plaintext.PlainTextFormatSimple;
import trainer.stream.plaintext.PlainTextFormatTwitter;
import trainer.stream.plaintext.StreamPlainTextWorker;
import twitter4j.Logger;
import utils.Pair;
import dictionary.approximatedDictionaries.ApproximatedDictionary;
import dictionary.dictionaryentry.DictionaryEntry;
import dictionary.exactDictionaries.ExactDictionary;
import dictionary.io.DictionaryIO;
import dictionary.ruleBasedDictionaries.RegExMatcher;
import dictionary.ruleBasedDictionaries.RuleBasedDictionary;

import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.JTextField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import configuration.ApproximatedDictionaryConfigurator;
import configuration.ExactDictionaryConfigurator;
import configuration.PreProcessConfigurator;
import configuration.RuleBasedConfigurator;
import configuration.SyntaxCheckerConfigurator;

public class MainWindow {

	private static final String titulo = "Tweets Analyzer";

	private JFrame frame;
	private String[] selectedFilePath = new String[]{ null, null};
	private StreamWorkerAbs streamWorker;
	private JTree tree;
	private Vector<String> tweets = new Vector<String>();
	private NER ner;

	private AnalyzerWorker analyzer;
	private JToggleButton btnProcess;

	private JMenuItem mntmTreeToText;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(titulo);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/examples/twitter_64x64.png")));
		frame.setBounds(0,0,java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width,java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpenTweets = new JMenuItem("Open Tweets");
		mntmOpenTweets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openTweets();
			}
		});

		JMenuItem mntmNew = new JMenuItem("Clear");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearTree();
			}
		});
		mnFile.add(mntmNew);

		JSeparator separator_2 = new JSeparator();
		mnFile.add(separator_2);
		mnFile.add(mntmOpenTweets);

		JMenuItem mntmOpenText = new JMenuItem("Open Text");
		mntmOpenText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openText();
			}
		});
		mnFile.add(mntmOpenText);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		mntmTreeToText = new JMenuItem("Tree to Text");
		mntmTreeToText.setEnabled(false);
		mntmTreeToText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				treeToText();
			}
		});
		mnTools.add(mntmTreeToText);

		JMenuItem mntmViewData = new JMenuItem("View Data");
		mnTools.add(mntmViewData);

		JMenu mnClassification = new JMenu("Classification");
		menuBar.add(mnClassification);

		JMenu mnClassifier = new JMenu("Classifier");
		mnClassification.add(mnClassifier);

		JRadioButtonMenuItem rdbtnmntmJ = new JRadioButtonMenuItem("J48");
		mnClassifier.add(rdbtnmntmJ);

		JRadioButtonMenuItem rdbtnmntmId = new JRadioButtonMenuItem("Id3");
		mnClassifier.add(rdbtnmntmId);

		JMenuItem mntmTrainClassifier = new JMenuItem("Train Classifier");
		mnClassification.add(mntmTrainClassifier);

		JSeparator separator_1 = new JSeparator();
		mnClassification.add(separator_1);

		JMenuItem mntmClassify = new JMenuItem("Classify");
		mnClassification.add(mntmClassify);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane, BorderLayout.CENTER);

		tree = new JTree();
		tree.setVisibleRowCount(30);
		tree.setFont(new Font("Consolas", Font.PLAIN, 12));
		tree.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tree.setModel(new DefaultTreeModel(
				new TweetDefaultMutableTreeNode("Open a file to process it") {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					{

					}
				}
				));
		tree.setCellRenderer(new TweetRenderer());
		scrollPane.setViewportView(tree);

		JPanel westPane = new JPanel();
		Border border = westPane.getBorder();
		Border margin = new EmptyBorder(0,10,5,10);
		westPane.setBorder(new CompoundBorder(border, margin));
		frame.getContentPane().add(westPane, BorderLayout.WEST);


		btnProcess = new JToggleButton("Process");
		btnProcess.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnProcess.setEnabled(false);
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				process(btnProcess.isSelected());
			}
		});
		BoxLayout boxLayout = new BoxLayout(westPane, BoxLayout.Y_AXIS);
		westPane.setLayout(boxLayout);
		westPane.setPreferredSize(new Dimension(250,50));

		JPanel configPanel = new JPanel();
		configPanel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 5, 0), new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Configuration", TitledBorder.CENTER, TitledBorder.TOP, null, null)));
		westPane.add(configPanel);

		JButton btnConfiguration = new JButton("Open Configuration File");
		btnConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configuration();
			}
		});
		configPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("145px:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
				new RowSpec[] {
				RowSpec.decode("7px"),
				RowSpec.decode("23px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		configPanel.add(btnConfiguration, "2, 2, fill, top");

		JLabel lblClassification = new JLabel("Classification");
		lblClassification.setFont(new Font("Tahoma", Font.PLAIN, 14));
		configPanel.add(lblClassification, "2, 4");

		textField = new JTextField();
		textField.setMargin(new Insets(3,3,3,3));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		configPanel.add(textField, "2, 6, fill, default");
		textField.setColumns(10);
		westPane.add(btnProcess);

	}

	//--------------------------------------------------------------------------------------------------------------------------------------------

	private void configuration() {
		//Open FileChoose
		JFileChooser chooser = new JFileChooser("./");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain Text (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(frame);
		String path="";
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getPath();
		}
		if (path.isEmpty())
			return;

		//Read File
		String configurationText="";
		Vector<String> lines = new Vector<String>();
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			file = new File (path);
			fr = new FileReader (file);
			br = new BufferedReader(fr);
			String line;
			while( (line=br.readLine()) != null ){
				if (!line.startsWith("#")){
					line = line.trim();
					if (!line.isEmpty()){
						configurationText+=line+"\n";
						lines.add(line);
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

		//Create de textDialog
		TextDialog textDialog = new TextDialog("Configuration [" + path + "]", configurationText,true);
		textDialog.setEditable(false);
		textDialog.setModal(true);
		textDialog.setLocationRelativeTo(null);
		textDialog.setVisible(true);
		configurationText = textDialog.getText();

		//Configure
		configure(lines);
	}

	private void configure(Vector<String> lines) {
		String fileEntries = null;
		boolean toLowerCase = false; //falta
		Vector<Pair<String,String>> preProcessRules = new Vector<Pair<String,String>>();
		String configurationExactDictionary = null;
		boolean doRuleBasedDictionary = false;
		Vector<RegExMatcher> ruleBasedRules = new Vector<RegExMatcher>();
		String configurationApproximatedDictionary = null;
		String configurationSyntaxChecker = null;
		Vector<Vector<String>> synonyms = new Vector<Vector<String>>();
		Vector<Pair<Vector<String>,String>> rulesSyntaxChecker = new Vector<Pair<Vector<String>,String>>();

		for (String line : lines){

			String option = line.split("=")[0].trim();
			String rightMember = line.split("=")[1].trim();

			switch (option){
			case "FILE-ENTRIES":
				fileEntries = rightMember;
				break;
			case "TO-LOWERCASE":
				if (rightMember.toLowerCase().equals("true")){
					toLowerCase=true;
				}
				break;
			case "PREPROCESS-RULE":
				String search = null;
				String replace = null;

				search = rightMember.split("-s")[1].split("-r")[0].trim();
				search = search.substring(1, search.length()-1);
				replace = rightMember.split("-r")[1].trim();
				replace = replace.substring(1, replace.length()-1);

				if (search != null && replace != null){
					preProcessRules.add(new Pair<String,String>(search,replace));
				}

				break;
			case "EXACT-DICTIONARY":
				configurationExactDictionary = rightMember;
				break;
			case "RULE-BASED-DICTIONARY":
				if (rightMember.toLowerCase().equals("true")){
					doRuleBasedDictionary = true;
				}
				break;
			case "RULE-BASED-DICTIONARY-RULE":
				String search1 = null;
				String means1 = null;

				search1 = rightMember.split("-s")[1].split("-m")[0].trim();
				search1 = search1.substring(1, search1.length()-1);
				means1 = rightMember.split("-m")[1].trim();
				means1 = means1.substring(1, means1.length()-1);
				
				if (search1 != null && means1 != null){
					ruleBasedRules.add(new RegExMatcher(search1, means1));
				}

				break;
			case "APPROXIMATED-DICTIONARY":
				configurationApproximatedDictionary = rightMember;
				break;
			case "SYNTAX-CHECKER":
				configurationSyntaxChecker = rightMember;
				break;
			case "SYNONYMS":
				Vector<String> arguments2 = Segmenter.getSegmentation(rightMember);
				Vector<String> cleanList = new Vector<String>();
				for (String arg : arguments2){
					cleanList.add(arg.substring(1, arg.length()-1));
				}
				synonyms.add(cleanList);				
				break;
			case "SYNTAX-CHECKER-RULE":
				//-s "Calle" "esquina" "Calle" -m "Interseccion"	
				Vector<String> searches = new Vector<String>();
				Vector<String> cleanSearches = new Vector<String>();
				
				String means3 = "";
				
				String toSearch = rightMember.split("-s")[1].split("-m")[0].trim();
				searches = Segmenter.getSegmentation(toSearch);
				for (String s : searches){
					cleanSearches.add(s.substring(1, s.length()-1));
				}
					
				means3 = rightMember.split("-m")[1].trim();
				means3 = means3.substring(1, means3.length()-1);
				
				rulesSyntaxChecker.addAll(SyntaxChecker.createRules(cleanSearches.toArray(new String[]{}), means3, synonyms));

				break;
			default :
				System.out.println("ERROR BITCH" + option);
			}

		}

		//Se crea todo aca
		if (fileEntries != null){

			//NER
			Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
			entradas.addAll(filesEntries(fileEntries));

			//Creación del PreProcess
			PreProcessConfigurator preProcessConfigurator = new PreProcessConfigurator(PreProcess.class.getName(), preProcessRules);
			PreProcess preProcess = (PreProcess) preProcessConfigurator.configure("");


			//Creación de Diccionarios
			//Diccionarios Exactos
			ExactDictionaryConfigurator eDC = new ExactDictionaryConfigurator(ExactDictionary.class.getName(),entradas);
			ExactDictionary dic1 = (ExactDictionary) eDC.configure(configurationExactDictionary);

			//Diccionarios basados en reglas
			RuleBasedConfigurator rBC = new RuleBasedConfigurator(RuleBasedDictionary.class.getName(), entradas, ruleBasedRules);
			RuleBasedDictionary dic2 = (RuleBasedDictionary) rBC.configure("");

			//Diccionarios Aproximados
			ApproximatedDictionaryConfigurator aDC = new ApproximatedDictionaryConfigurator(ApproximatedDictionaryConfigurator.class.getName(), entradas);
			ApproximatedDictionary dic3 = (ApproximatedDictionary) aDC.configure(configurationApproximatedDictionary);

			//Creación del SyntaxChecker
			SyntaxCheckerConfigurator syntaxCheckerConfigurator = new SyntaxCheckerConfigurator(SyntaxChecker.class.getName(), null);
			SyntaxChecker syntaxChecker = (SyntaxChecker) syntaxCheckerConfigurator.configure(configurationSyntaxChecker);
			syntaxChecker.addRules(rulesSyntaxChecker);


			//Creación del NER
			ner = new NER(true);
			ner.addDictionary(dic1);
			ner.addDictionary(dic2);
			ner.addDictionary(dic3);
			ner.setSyntaxChecker(syntaxChecker);
			ner.setPreProcess(preProcess);
			ner.setDoPreProcess(!preProcessRules.isEmpty());
			ner.setToLowerCase(toLowerCase);

		}


	}



	private void treeToText() {
		if (tweets != null && tweets.size() > 0 ){
			String text = treeToText((TweetDefaultMutableTreeNode) tree.getModel().getRoot(),0);
			TextDialog textDialog = new TextDialog("Tree to Text",text,false);
			textDialog.setVisible(true);
			textDialog.setLocationRelativeTo(null);
		}

	}

	private String treeToText(TweetDefaultMutableTreeNode node,int level){
		String aux="";
		String out="";
		if (level > 0)
			aux+="└";
		for (int i = 0; i < level; i++)
			aux+="─";
		out =aux + node.toString() + "<br>";
		Enumeration<?> childrens = node.children();
		while (childrens.hasMoreElements()){
			TweetDefaultMutableTreeNode nextElement = (TweetDefaultMutableTreeNode) childrens.nextElement();
			out += treeToText(nextElement,level + 1);
		}
		return out;
	}

	private void clearTree() {
		tree.setModel(new DefaultTreeModel(
				new TweetDefaultMutableTreeNode("Open a file to process it") {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					{

					}
				}
				));
		tweets.clear();
		btnProcess.setEnabled(false);
		mntmTreeToText.setEnabled(false);
	}

	private void openText() {
		selectFile();
		load(0);
	}

	private void openTweets() {
		selectFile();
		load(1);	
	}

	protected void selectFile() {
		JFileChooser chooser = new JFileChooser("./");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain Text (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFilePath[0] = chooser.getSelectedFile().getPath();
			selectedFilePath[1] = chooser.getSelectedFile().getName();
		}
	}

	private void load(int option){
		PlainTextFormatAbs format = null;
		switch(option){
		case 0:
			format = new PlainTextFormatSimple();
			break;
		case 1:
			format = new PlainTextFormatTwitter();
			break;
		}
		this.streamWorker = new StreamPlainTextWorker(this.selectedFilePath[0], format);
		//this.streamWorker.setCounter(this.lblCounter);
		this.streamWorker.start();

		//TODO dormirlo para que el streamworker llegue a estar disponible para levantar tweets
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Create the tree
		frame.setTitle(titulo + " - [Cargando...]");
		createTree();
		frame.setTitle(titulo);

		mntmTreeToText.setEnabled(!tweets.isEmpty());
		btnProcess.setEnabled(!tweets.isEmpty());
	}

	private void createTree(){
		TweetDefaultMutableTreeNode root = new TweetDefaultMutableTreeNode("Tweets");
		DefaultTreeModel model = new DefaultTreeModel(root);
		this.tweets.clear();

		String tweet = null;	
		Integer i = 1;
		while ( ( tweet = this.streamWorker.getNextTweet()) != null ){
			tweets.add(tweet);
			TweetDefaultMutableTreeNode node = new TweetDefaultMutableTreeNode("<span style=\"color:blue\"><b>[" + i + "]</b></span> [" + tweet + "] ");
			root.add(node);
			i++;
		}
		root.setText(root.getText() + " [" + (i - 1) + "]");
		tree.setModel(model);
		tree.validate();
	}


	private void process(boolean isSelected) {
		switchButton(!isSelected);
		if (!isSelected){

			if (analyzer != null){
				analyzer.stopRunning();
				while (analyzer.isAlive()){

				}
				analyzer = null;

			}
		}
		else{
			analyzer = new AnalyzerWorker(tweets, ner, tree);
			analyzer.start();
		}
	}

	private void switchButton(boolean isSelected) {

		if (isSelected)
			btnProcess.setText("Process");
		else
			btnProcess.setText("Stop");
	}


	//---------------------------------------------------------------------------------------------------

	// Cosas Copiadas del Main
	private static NER createNER(Vector<DictionaryEntry> entries){

		//Creación del PreProcess
		PreProcess preProcess = new PreProcess();
		preProcess.addRule(new Pair<String, String>("(\\D)([\\.,])(\\D)?","$1 $2 $3"));
		preProcess.addRule(new Pair<String, String>(" {2,}"," "));


		//Creación de Diccionarios
		//Diccionarios Exactos
		ExactDictionary dic1 = new ExactDictionary(entries,false,true);

		//Diccionarios basados en reglas
		RuleBasedDictionary dic2 = new RuleBasedDictionary();
		dic2.addMatcher(new RegExMatcher("[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})","Mail"));
		dic2.addMatcher(new RegExMatcher("[0-9]+","numero"));
		dic2.addMatcher(new RegExMatcher("\\sy+","y"));
		dic2.addMatcher(new RegExMatcher("al","al"));
		dic2.addMatcher(new RegExMatcher("la","la"));
		dic2.addMatcher(new RegExMatcher("esquina","esquina"));
		dic2.addMatcher(new RegExMatcher("entre","entre"));
		dic2.addMatcher(new RegExMatcher("casi","casi"));
		dic2.addMatcher(new RegExMatcher("de","de"));		

		//Diccionarios Aproximados
		ApproximatedDictionary dic3 = new ApproximatedDictionary(entries, 0.6, 2, 1,false);

		//Creación del SyntaxChecker
		SyntaxChecker syntaxChecker = createSyntaxChecker();		

		//Creación del NER
		NER ner = new NER(true);
		ner.addDictionary(dic1);
		ner.addDictionary(dic2);
		ner.addDictionary(dic3);
		ner.setSyntaxChecker(syntaxChecker);
		ner.setPreProcess(preProcess);
		ner.setDoPreProcess(true);
		ner.setToLowerCase(true);

		return ner;
	}

	private static Vector<DictionaryEntry> userEntries(){
		//Entradas agregadas por el usuario		
		Vector<DictionaryEntry> entradas = new Vector<DictionaryEntry>();
		entradas.add(new DictionaryEntry("Maxi Duthey",new String[]{"Persona"}));
		entradas.add(new DictionaryEntry("Brian Caimmi",new String[]{"Persona"}));
		entradas.add(new DictionaryEntry("Tandil",new String[]{"Localidad"}));
		entradas.add(new DictionaryEntry("Jujuy",new String[]{"Localidad"}));

		return entradas;
	}

	private static Vector<DictionaryEntry> filesEntries(String path){
		//Entradas levantadas desde archivo
		Set<DictionaryEntry> entries = new HashSet<DictionaryEntry>();
		/*entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/callesTandil.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/colores.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/marcasDeAutos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/modelosDeAutos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/rutas.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/marcasDeMotos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/corpusDeVehiculos.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/tipo_calle.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/tipo_persona.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/evento_demora.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/evento_accidente.txt"));
		entries.addAll(DictionaryIO.loadPlainTextWithCategories("dics/calles_BA.txt"));*/


		entries.addAll(DictionaryIO.loadPlainTextWithCategories(path));
		return new Vector<DictionaryEntry>(entries);
	}

	private static SyntaxChecker createSyntaxChecker(){

		//Here we specify the synonyms
		Vector<Vector<String>> synonyms = new Vector<Vector<String>>();
		Vector<String> synonym1 = new Vector<String>(Arrays.asList(new String[]{"Calle", "Ruta" , "Avenida", "Autopista"}));
		synonyms.add(synonym1);

		//In this section we create the rules
		Vector<Pair<Vector<String>,String>> rules = new Vector<Pair<Vector<String>,String>>();
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "esquina", "Calle"},"Interseccion",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "y", "Calle"},"Interseccion",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "entre", "Calle", "y", "Calle"},"Direccion Indeterminada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "casi", "Calle"},"Direccion Indeterminada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "numero"},"Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "al", "Numero"}, "Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "a", "la", "Numero"}, "Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"Calle", "al", "Numero", "Entre", "Calle", "y", "Calle"},"Direccion Determinada",synonyms));
		rules.addAll(SyntaxChecker.createRules(new String[]{"numero", "de", "Calle"},"Direccion Determinada",synonyms));

		//Then we create the syntaxChecker
		SyntaxChecker syntaxChecker = new SyntaxChecker();
		syntaxChecker.addRules(rules);

		return syntaxChecker;

	}

}
