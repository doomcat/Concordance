package uk.ac.aber.dcs.odj.concordance;

/**
 * Smallest object needed to store line & context data for
 * any given word. Extends {@link CustomNode} so can be used
 * in a {@link LittleLinkedList} - next() is both a getter &
 * setter for the next CustomNode in the chain.
 * @author Owain Jones <odj@aber.ac.uk>
 *
 */
public class WordEntry extends CustomNode {
	private int line;
	private String context;
	
	public WordEntry() {
		this(0,null);
	}
	
	public WordEntry(int line) {
		this(line,null);
	}
	
	public WordEntry(int line, String context) {
		this.line = line;
		this.context = context;
	}
	
	/**
	 * @return the line number (int) on which the word has been found
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * @param line (int) the line number on which the word has been found
	 */
	public void setLine(int line) {
		this.line = line;
	}
	
	/**
	 * @return the context (String) in which the word has been found -
	 * the sentence surrounding the word.
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * @param context (String) the context in which the word has been found -
	 * the sentence surrounding the word.
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	/**
	 * Does a deep comparison to check whether two WordEntries are looking at
	 * the same line and sentence chunk
	 * @param other The other WordEntry to compare to
	 * @return true if both WordEntries are identical, false if not
	 */
	@Override
	public boolean identical(CustomNode other) {
		WordEntry tother = (WordEntry) other;
		if(this.line == tother.line && this.context == tother.context) {
			return true;
		}
		return false;
	}
	
}
