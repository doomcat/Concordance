package uk.ac.aber.dcs.odj.concordance.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.StyledDocument;

import uk.ac.aber.dcs.odj.concordance.Concordance;
import uk.ac.aber.dcs.odj.concordance.LittleLinkedList;
import uk.ac.aber.dcs.odj.concordance.WordEntry;

public class MainWindow implements ActionListener, ListSelectionListener {
	private JFrame window;
	private Container content;
	private Concordance concordance;
	
	private String fDocument;
	private String fIndex;
	
	private JButton bOpenFile;
	private JButton bOpenIndex;
	private JButton bScan;
	private JButton bAddWord;
	
	private JLabel lOpenFile;
	private JLabel lOpenIndex;
	
	private JTextField tAddWord;
	
	private DefaultListModel indexes;
	private JList indexList;
	private JList indexCountList;
	
	private JTextArea output;
	
	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
	}
	
	public MainWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Couldn't make Swing look like your OS theme");
		}
		
		window = new JFrame("Concordance Generator");
		window.setSize(768, 560);
		content = window.getContentPane();
		//content.setLayout(new GridBagLayout());
		
		JPanel topBar = new JPanel();
		topBar.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();				
		bOpenFile = new JButton("Open Document...");
		bOpenFile.addActionListener(this);
		c.gridx = 0; c.gridy = 0; c.anchor = c.BASELINE_LEADING; c.fill = c.VERTICAL;
		c.ipadx = 2; c.ipady = 2;
		topBar.add(bOpenFile, c);
		
		bOpenIndex = new JButton("Open Index File...");
		bOpenIndex.addActionListener(this);
		c.gridx = 0; c.gridy = 1; c.anchor = c.BASELINE_LEADING; c.fill = c.VERTICAL;
		topBar.add(bOpenIndex, c);
		
		lOpenFile = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 0; c.anchor = c.CENTER; c.weightx = 1; c.fill = c.BOTH;
		topBar.add(lOpenFile, c);
		
		lOpenIndex = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 1; c.anchor = c.CENTER; c.weightx = 1; c.fill = c.BOTH;
		topBar.add(lOpenIndex, c);

		bScan = new JButton("Scan");
		bScan.addActionListener(this);
		c.gridx = 2; c.gridy = 0; c.gridheight = 2; c.weightx = 0;
		topBar.add(bScan, c);
		
		JSeparator separator = new JSeparator();
		c.gridx = 0; c.gridy = 2; c.gridheight = 1; c.gridwidth = 3; c.weightx = 1;
		topBar.add(separator, c);

		content.add(topBar, BorderLayout.NORTH);
		
		indexes = new DefaultListModel();
		indexList = new JList(indexes);
		indexList.addListSelectionListener(this);
		indexCountList = new JList();
		indexCountList.setEnabled(false);
		
		JPanel listPane = new JPanel();
		listPane.setLayout(new BorderLayout());
		listPane.add(indexList, BorderLayout.CENTER);
		listPane.add(indexCountList, BorderLayout.WEST);
		
		JScrollPane iPane1 = new JScrollPane(listPane);
		JPanel pane1 = new JPanel();
		
		pane1.setLayout(new BorderLayout());
		pane1.add(iPane1, BorderLayout.CENTER);
		
		JPanel ipane2 = new JPanel();
		ipane2.setLayout(new BorderLayout());
		
		tAddWord = new JTextField();
		ipane2.add(tAddWord, BorderLayout.CENTER);
		
		bAddWord = new JButton("+");
		bAddWord.addActionListener(this);
		ipane2.add(bAddWord, BorderLayout.LINE_END);
		
		pane1.add(ipane2, BorderLayout.PAGE_END);
		
		output = new JTextArea();
		output.setEditable(false);
		output.setWrapStyleWord(true);
		output.setLineWrap(true);
		
		JScrollPane pane2 = new JScrollPane(output);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				pane1, pane2);
		split.setOneTouchExpandable(true); split.setDividerLocation(120);
		c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
		c.weightx = 1; c.fill = c.BOTH; c.anchor = c.BASELINE;
		content.add(split, BorderLayout.CENTER);
		
		window.setVisible(true);
	}
	
	private void printReferences(String key) {
		try{
			if(concordance.containsKey(key)) {
				output.setText("");
				LittleLinkedList<WordEntry> list = (LittleLinkedList) concordance.get(key);
				WordEntry e = (WordEntry) list.getHead();
				while(e != null) {
					String out = "Line "+e.getLine()+"\n----\n"+e.getContext()+"\n\n";
					output.append(out);
					e = (WordEntry) e.next();
				}
			} else {
				output.setText("The word '"+key+"' was not found in this document.");
			}
		} catch(Exception e) {
			output.setText("Concordance not loaded properly yet, try clicking 'Scan'");
		}
	}
	
	private void scan() {
		Object[] tmpIndex = indexes.toArray();
		String[] index = new String[tmpIndex.length];
		for(int i=0; i<tmpIndex.length; i++) {
			index[i] = tmpIndex[i].toString();
		}
		try {
			concordance = new Concordance(fDocument, (String[]) index);
			concordance.scan();
			printCounts();
		} catch (IOException e) {
			// TODO Implement error dialog
			e.printStackTrace();
		}
	}

	private String chooseFile(String oldVal) {
		JFileChooser fc = new JFileChooser();
		try {
			fc.setCurrentDirectory(new File(".").getCanonicalFile());
			int status = fc.showOpenDialog(window);
			if(status == JFileChooser.APPROVE_OPTION) {
				return fc.getSelectedFile().getAbsolutePath();
			}
		} catch (IOException e) {
			// TODO Add dialog
			e.printStackTrace();
		}
		return oldVal;
	}
	
	private void printCounts() {
		indexCountList.setListData(new Object []{});
		Vector<Integer> counts = new Vector<Integer>();
		for(Object s : indexes.toArray()) {
			if(concordance.containsKey(s)) {
				counts.add(concordance.get(s).size());
			} else {
				counts.add(0);
			}
		}
		indexCountList.setListData(counts);
	}
	
	private void parseIndex(String file) {
		ArrayList<String> words = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			Vector<Object> tmpCount = new Vector<Object>();
			indexes.clear();
			indexCountList.setListData(tmpCount);
			while((line = in.readLine()) != null) {
				if(concordance.isWord(line)) {
					words.add(line.toLowerCase());
					tmpCount.add(0);
				}
			}
			Collections.sort(words);
			for(String word : words) indexes.addElement(word);
		} catch (Exception e) {
			// TODO Add dialog
			e.printStackTrace();
		}
	}
	
	private void addWord(String word) {
		if(concordance.isWord(word)) {
			indexes.addElement(word.toLowerCase());
			Object[] words = indexes.toArray();
			Arrays.sort(words);
			indexes.clear();
			for(Object w : words) indexes.addElement(w.toString());
			printCounts();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bOpenFile) {
			fDocument = chooseFile(fDocument);
			lOpenFile.setText(fDocument);
		} else if(e.getSource() == bOpenIndex) {
			fIndex = chooseFile(fIndex);
			lOpenIndex.setText(fIndex);
			parseIndex(fIndex);
		} else if(e.getSource() == bAddWord) {
			addWord(tAddWord.getText());
			tAddWord.setText(null);
		} else if(e.getSource() == bScan) scan();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		String key = ((String)((JList) e.getSource()).getSelectedValue());
		printReferences(key);
	}

}
