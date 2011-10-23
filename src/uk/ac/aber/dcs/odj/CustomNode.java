package uk.ac.aber.dcs.odj;

/**
 * Interface to build 'Custom Node' elements for Linked Lists and other ADTs.
 * Based on the idea that you might want to avoid the encapsulation that Java's
 * own Node implementations have.
 * @author odj
 *
 */
public interface CustomNode {
	public CustomNode next();
	public void next(CustomNode node);
}
