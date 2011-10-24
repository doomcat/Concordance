package uk.ac.aber.dcs.odj.concordance.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
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
		c.gridx = 0; c.gridy = 0; c.anchor = c.NORTHWEST; c.fill = c.BOTH;
		c.ipadx = 2; c.ipady = 2;
		content.add(bOpenFile, c);
		
		JButton bOpenIndex = new JButton("Open Index File...");
		c.gridx = 0; c.gridy = 1; c.anchor = c.NORTHWEST;
		content.add(bOpenIndex, c);
		
		JLabel lOpenFile = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 0; c.anchor = c.NORTH; c.weightx = 1;
		content.add(lOpenFile, c);
		
		JLabel lOpenIndex = new JLabel("No file specified");
		c.gridx = 1; c.gridy = 1; c.anchor = c.NORTH; c.weightx = 1;
		content.add(lOpenIndex, c);
		
		c.weightx = 0;
		
		JButton bScan = new JButton("Scan");
		c.gridx = 2; c.gridy = 0; c.gridheight = 2; c.anchor = c.NORTHEAST;
		content.add(bScan, c);
		
		JSeparator separator = new JSeparator();
		c.gridx = 0; c.gridy = 2; c.gridwidth = 3; c.weightx = 1;
		content.add(separator, c);
		
		JLabel pane1 = new JLabel("Hello");
		JLabel pane2 = new JLabel("World");
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				pane1, pane2);
		split.setOneTouchExpandable(true); split.setDividerLocation(260);
		c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
		c.weightx = 1; c.fill = c.BOTH;
		content.add(split, c);
		
		window.setVisible(true);
	}

}
