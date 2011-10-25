package uk.ac.aber.dcs.odj.concordance.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

public class MainWindow {
	private static JFrame window;
	private static Container content;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Couldn't make Swing look like your OS theme");
		}
		
		window = new JFrame("Concordance Generator");
		content = window.getContentPane();
		content.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();				
		JButton bOpenFile = new JButton("Open Document...");
		c.gridx = 0; c.gridy = 0; c.anchor = c.BASELINE_LEADING; c.fill = c.BOTH;
		c.ipadx = 2; c.ipady = 2;
		content.add(bOpenFile, c);
		
		JButton bOpenIndex = new JButton("Open Index File...");
		c.gridx = 0; c.gridy = 1; c.anchor = c.BASELINE_LEADING;
		content.add(bOpenIndex, c);
		
		JLabel lOpenFile = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 0; c.anchor = c.CENTER; c.weightx = 1;
		content.add(lOpenFile, c);
		
		JLabel lOpenIndex = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 1; c.anchor = c.CENTER; c.weightx = 1;
		content.add(lOpenIndex, c);
		
		c.weightx = 0;
		
		JButton bScan = new JButton("Scan");
		c.gridx = 2; c.gridy = 0; c.gridheight = 2;
		content.add(bScan, c);
		
		JSeparator separator = new JSeparator();
		c.gridx = 0; c.gridy = 2; c.gridheight = 1; c.gridwidth = 3; c.weightx = 1;
		content.add(separator, c);

		DefaultListModel indexes = new DefaultListModel();
		JList indexList = new JList(indexes);
		indexes.addElement("Hello");
		indexes.addElement("World");
		
		JScrollPane pane1 = new JScrollPane(indexList);
		JScrollPane pane2 = new JScrollPane();
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				pane1, pane2);
		split.setOneTouchExpandable(true); split.setDividerLocation(260);
		c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
		c.weightx = 1; c.fill = c.BOTH;
		content.add(split, c);
		
		JPanel pAddWord = new JPanel();
		pAddWord.setLayout(new FlowLayout());
		JTextField tAddWord = new JTextField();
		JButton bAddWord = new JButton("Add");
		pAddWord.add(tAddWord); pAddWord.add(bAddWord);
		c.gridx = 0; c.gridy = 5; c.gridwidth = 1; c.anchor = c.BASELINE_LEADING;
		c.weightx = 0.5; c.fill = c.BOTH;
		content.add(pAddWord, c);
		
		window.setVisible(true);
	}

}
