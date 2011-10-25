package uk.ac.aber.dcs.odj.concordance.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.UIManager;

import uk.ac.aber.dcs.odj.concordance.Concordance;

public class MainWindow implements ActionListener {
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
		content = window.getContentPane();
		content.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();				
		bOpenFile = new JButton("Open Document...");
		bOpenFile.addActionListener(this);
		c.gridx = 0; c.gridy = 0; c.anchor = c.BASELINE_LEADING; c.fill = c.VERTICAL;
		c.ipadx = 2; c.ipady = 2;
		content.add(bOpenFile, c);
		
		bOpenIndex = new JButton("Open Index File...");
		bOpenIndex.addActionListener(this);
		c.gridx = 0; c.gridy = 1; c.anchor = c.BASELINE_LEADING; c.fill = c.VERTICAL;
		content.add(bOpenIndex, c);
		
		lOpenFile = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 0; c.anchor = c.CENTER; c.weightx = 1; c.fill = c.BOTH;
		content.add(lOpenFile, c);
		
		lOpenIndex = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 1; c.anchor = c.CENTER; c.weightx = 1; c.fill = c.BOTH;
		content.add(lOpenIndex, c);

		bScan = new JButton("Scan");
		bScan.addActionListener(this);
		c.gridx = 2; c.gridy = 0; c.gridheight = 2; c.weightx = 0;
		content.add(bScan, c);
		
		JSeparator separator = new JSeparator();
		c.gridx = 0; c.gridy = 2; c.gridheight = 1; c.gridwidth = 3; c.weightx = 1;
		content.add(separator, c);

		indexes = new DefaultListModel();
		JList indexList = new JList(indexes);
		
		JScrollPane iPane1 = new JScrollPane(indexList);
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
		
		JScrollPane pane2 = new JScrollPane();
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				pane1, pane2);
		split.setOneTouchExpandable(true); split.setDividerLocation(260);
		c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
		c.weightx = 1; c.fill = c.BOTH;
		content.add(split, c);
		
		window.setVisible(true);
	}
	
	private void scan() {
		concordance = new Concordance(fDocument);
		concordance.setFile();
	}

	private String chooseFile(String oldVal) {
		JFileChooser fc = new JFileChooser();
		int status = fc.showOpenDialog(window);
		if(status == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return oldVal;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bOpenFile) {
			fDocument = chooseFile(fDocument);
			lOpenFile.setText(fDocument);
		} else if(e.getSource() == bOpenIndex) {
			fIndex = chooseFile(fIndex);
			lOpenIndex.setText(fIndex);
		} else if(e.getSource() == bAddWord) {
			String word = tAddWord.getText();
			if(concordance.isWord(word)) {
				indexes.addElement(word);
				tAddWord.setText(null);
			}
		}
	}

}
