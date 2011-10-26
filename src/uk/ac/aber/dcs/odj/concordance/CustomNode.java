package uk.ac.aber.dcs.odj.concordance;

/**
 * Interface to build 'Custom Node' elements for Linked Lists and other ADTs.
 * Based on the idea that you might want to avoid the encapsulation that Java's
 * own Node implementations have - instead of having a parameter (e.g. 'data')
 * that's a reference to an underlying Object, you have multiple parameters in
 * which to store your data, eliminating the need for extra reference variables/
 * instantiated objects.
 * @author Owain Jones <odj@aber.ac.uk>
 *
 */
public abstract class CustomNode {
	private CustomNode next;

	/**
	 * next() without any arguments gets the next CustomNode in the chain, or
	 * returns null if there isn't one (e.g. we're at the end of the list).
	 * @return the next CustomNode in the chain.
	 */
	public CustomNode next() {
		return this.next;
	}
	
	/**
	 * next() with a CustomNode argument sets the next CustomNode in the chain
	 * to the 'node' parameter
	 * @param node CustomNode to set
	 */
	public void next(CustomNode node) {
		this.next = node;
	}
	
	/**
	 * Do a deep equality comparison to check whether two objects are the same.
	 * Used in {@link LittleLinkedList} to stop us adding the same thing twice.
	 * Children of this class have to implement this method to actually do any
	 * checking.
	 * @param node The CustomNode offspring to compare against
	 * @return false by default
	 */
	public boolean identical(CustomNode node) {
		return false;
	}
}
