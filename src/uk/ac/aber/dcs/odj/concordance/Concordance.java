package uk.ac.aber.dcs.odj.concordance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import java.util.LinkedList;

public class Concordance extends Hashtable<String,LittleLinkedList>{
	private String file;
	private String[] index;
	private BufferedReader buffer;
	
	public Concordance(String file, String[] index) throws IOException {
		super();
		this.index = index;
		this.generateIndex();
		this.setFile(file);
	}
	
	public void setFile(String file) throws IOException {
		InputStream in = new FileInputStream(new File(file));
		Reader reader = new InputStreamReader(in);
		this.buffer = new BufferedReader(reader);
		this.file = file;
	}
	
	public void setIndex(String[] index) {
		this.index = index;
	}
	
	public void generateIndex() {
		for(String s : this.index) {
			this.put(s.toLowerCase(), new LittleLinkedList<WordEntry>());
		}
	}

	public void scan() throws IOException {
		int b, line = 1;
		char c;
		StringBuilder currentWord = new StringBuilder();
		StringBuilder currentSentence = new StringBuilder();
		LinkedList<WordEntry> indexedWords = new LinkedList<WordEntry>();

		while((b = this.buffer.read()) != -1) {
			c = (char) b;
			currentSentence.append(c);
			if((c >= 'a' && c <= 'z')
				|| (c >= 'A' & c <= 'Z')
				|| (c >= '0' & c <= '9')) {
				currentWord.append(c);
			} else if(c == '-' || c == '\'') {
				if(currentWord.length() != 0) {
					currentWord.append(c);
				}
			} else if(c == ' ' || c == '\t') {
				if(currentWord.length() != 0) {
					String s = currentWord.toString().toLowerCase();
					if(this.containsKey(s)) {
						WordEntry entry = new WordEntry(line);
						indexedWords.add(entry);
						this.get(s).add(entry);
					}
				}
				currentWord = new StringBuilder();
			} else if(c == '\n') {
				line++;
				for(WordEntry w : indexedWords) {
					w.setContext(currentSentence.toString());
				}
				currentSentence = new StringBuilder();
				indexedWords.clear();
				continue;
			}
			/*if(c == '.' || c == '!' || c == '?' || c == ':' || c == '{'
				|| c == '}' || c == '(' || c == ')' || c == '[' || c == ']') {
				for(WordEntry w : indexedWords) {
					w.setContext(currentSentence.toString());
				}
				currentSentence = new StringBuilder();
				indexedWords.clear();
			}*/
		}
	}
}
