package uk.ac.aber.dcs.odj.concordance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * A class based on {@link Hashtable}, which when supplied with a filename and
 * an array of index words (as Strings), can generate a Concordance of the text
 * - the number of times a word appears in the text is counted, along with its
 * line numbers and contexts (the sentence/sentence fragment in which the word
 * was found.
 * @author Owain Jones <odj@aber.ac.uk>
 *
 */
public class Concordance extends Hashtable<String,LittleLinkedList<WordEntry>>{
	private String file;
	private String[] index;
	private BufferedReader buffer;
	
	/**
	 * Creates an empty Concordance ({@link Hashtable}) object with no
	 * keys or values.
	 */
	public Concordance() {
		super();
	}
	
	/**
	 * <p>Creates a new Concordance and populates the underlying
	 * {@link Hashtable} keys with the array of words (Strings) given in the
	 * {@code index} argument.</p>
	 * <p>The {@link Hashtable} keys (the index) are the words you want
	 * the Concordance to scan for in the document.</p>
	 * @param file The document (Absolute file path) for the Concordance to scan
	 * @param index The String[] array to generate the {@link Hashtable} keys
	 * from
	 * @throws IOException if file couldn't be loaded for some reason.
	 */
	public Concordance(String file, String[] index) throws IOException {
		super();
		this.index = index;
		this.generateIndex();
		this.setFile(file);
	}

	/**
	 * Prepare the Concordance to read from the specified file - opens the file
	 * ready for reading.
	 * @param file Absolute file path for file to open
	 * @throws IOException if file couldn't be loaded for some reason.
	 */
	public void setFile(String file) throws IOException {
		InputStream in = new FileInputStream(new File(file));
		Reader reader = new InputStreamReader(in);
		this.buffer = new BufferedReader(reader);
		this.file = file;
	}
	
	/**
	 * Set the list of words to scan for (Concordance words index)
	 * @param index String array of words
	 */
	public void setIndex(String[] index) {
		this.index = index;
	}
	
	/**
	 * For every word in the index, create a respective key in the Concordance's
	 * underlying {@link Hashtable}. This key is associated with an empty
	 * {@link LittleLinkedList}. (The LittleLinkedList is the key's value)
	 */
	public void generateIndex() {
		for(String s : this.index) {
			this.put(s.toLowerCase(), new LittleLinkedList<WordEntry>());
		}
	}

	/**
	 * Check if a character is alpha-numeric (a-z, A-Z, 0-9)
	 * @param c char to check
	 * @return true or false
	 */
	public static boolean isAlphaNumeric(char c) {
		return ((c >= 'a' && c <= 'z')
			|| (c >= 'A' && c <= 'Z')
			|| (c >= '0' && c <= '9'));
	}

	/**
	 * Check if a character is a legal part of a word (alphanumeric characters,
	 * plus apostrophes (') and hyphens (-)
	 * @param c char to check
	 * @return true or false
	 */
	public static boolean isWordChar(char c) {
		return (isAlphaNumeric(c) || c == '\'' || c == '-');
	}
	
	/**
	 * Check if a character is a punctuation symbol: full stops (.), periods
	 * (,), exclamation marks, question marks, [semi]colons, and the different
	 * forms of brackets are all counted as punctuation.
	 * @param c char to check
	 * @return true or false
	 */
	public static boolean isPunctuation(char c) {
		return (c == '.' || c == ',' || c == '!' || c == '?' || c == ':'
				|| c == ';' || c == '[' || c == ']' || c == '(' || c == ')'
				|| c == '{' || c == '}');
	}
	
	/**
	 * Check if a string is a word, grammatically: if the string consists of
	 * ONLY alphanumeric characters, plus apostrophes (') and hyphens (-), it
	 * is a word. If any other characters are found (such as whitespace),
	 * it isn't a word.
	 * @param s The String to check
	 * @return true if string is word, false if not
	 */
	public static boolean isWord(String s) {
		if(s.isEmpty()) return false;
		for(int i=0; i<s.length(); i++) {
			if(!isWordChar(s.charAt(i))) return false;
		}
		return true;
	}

	/**
	 * <p>Scan through the text file defined in the Concordance constructor or
	 * {@code setFile}, character by character. Looks for words which are
	 * listed in the concordance's index (using Hashtable.containsKey(word))
	 * and adds the references (line number and context) as {@link WordEntry}
	 * objects to that word's respective {@link LittleLinkedList}.</p>
	 * <p>The contexts for every {@link WordEntry} are generated based on
	 * sentence fragment boundaries - the algorithm assumes that, whenever a
	 * punctuation character is found, it's seperating two pieces of text, so
	 * contexts are the character range from one piece of punctuation (e.g. a
	 * full stop) to the next (e.g. a comma or a full-stop).</p>
	 * <p><b>Example:</b><br />
	 * {@code "That's not the most grandiose of natural language processing",
	 * said Owain} - if we were looking for 'grandiose' or 'processing', their
	 * contexts would both be the part within the speech marks.</p>
	 * <p>scan() doesn't return anything, because the internal state of this
	 * Concordance (a {@link Hashtable} after all) is altered by the method.
	 * Once scan() has returned, you can access the elements as you would a
	 * normal Hashtable.</p>
	 * <p><b>Example:</b><br />
	 * {@code Concordance.scan();
	 * Concordance.get("word").size(); // get the number of references to this
	 * word found after running a scan()}</p>
	 * @throws IOException if something happens to the document file (e.g. it's
	 * modified whilst we're reading it)
	 */
	public void scan() throws IOException {
		int b, line = 1;
		char c;
		StringBuilder currentWord = new StringBuilder();
		StringBuilder currentSentence = new StringBuilder();
		String s;
		LinkedList<WordEntry> indexedWords = new LinkedList<WordEntry>();
		boolean append;

		while((b = this.buffer.read()) != -1) { //do until EOF reached
			append = true;
			c = (char) b; //cast buffer int to char
			
			//if character is alphanumeric, add to 'current word' list
			if(isAlphaNumeric(c)) {
				currentWord.append(c);
			}
			
			/** if it's punctuation which could be inside a word (apostrophes
			 * and hyphens), and the 'current word' isn't empty, add to 
			 * 'current word' list. **/
			else if(c == '-' || c == '\'') {
				if(currentWord.length() != 0) {
					currentWord.append(c);
				}
			}
			/** if it's whitespace or punctuation, assume it's starting or ending
			 * a word. loop through the 'current word' to see if any part of it
			 * is a word in our index (so that words are found within their
			 * plural versions, for example 'cat' found in 'cats').
			 * If a word is found, create a WordEntry reference to it, and add
			 * it to an 'indexed words' list (used later on), and also add it
			 * to the concordance. **/
			else if(c == ' ' || c == '\t' || isPunctuation(c)) {
				for(int i=0; i<=currentWord.length(); i++) {
					s = currentWord.substring(i).toLowerCase();
					if(this.containsKey(s)) {
						WordEntry entry = new WordEntry(line);
						indexedWords.add(entry);
						this.get(s).add(entry);
					}
				}
				//since it's the end of a word, clear the 'current word' list.
				currentWord = new StringBuilder();
			}
			
			//if it's a line-terminating char, increment line number
			else if(c == '\n') line++; 
			
			/** if it's a punctuation character, assume it's starting/ending a
			 * sentence fragment. loop through the 'indexed words' list we've
			 * been adding discovered words to, and set the current sentence
			 * fragment as their context. then clear the current sentence
			 * buffer, and the 'indexed words' list. **/
			if(isPunctuation(c)) {
				for(WordEntry w : indexedWords) {
					w.setContext(currentSentence.toString());
				}
				currentSentence = new StringBuilder();
				append = false;
				indexedWords.clear();
			}
			
			//unless told otherwise, append char to 'current sentence' buffer.
			if(append) currentSentence.append(c);
		}
	}
}
