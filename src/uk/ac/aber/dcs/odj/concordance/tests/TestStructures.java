package uk.ac.aber.dcs.odj.concordance.tests;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.odj.concordance.CustomNode;
import uk.ac.aber.dcs.odj.concordance.LittleLinkedList;
import uk.ac.aber.dcs.odj.concordance.WordEntry;

public class TestStructures {
	final long NUM_ITERATIONS = 1000000; //how many times to run 'for' loops
	private LittleLinkedList<WordEntry> list;
	private WordEntry first;
	private WordEntry second;
	private WordEntry third;
	
	@Before
	public void setUp() throws Exception {
		first = new WordEntry();
		second = new WordEntry(1);
		third = new WordEntry(10,"hello world");
		list = new LittleLinkedList<WordEntry>();
	}

	@Test
	public void testAddingToList() {
		list.add(first);
		Assert.assertEquals(1,list.size());
		list.add(second);
		Assert.assertEquals(2,list.size());
		list.clear();
		list.add(third);
		Assert.assertEquals(1,list.size());
	}
	
	@Test
	public void testIteration() {
		list.add(first);
		list.add(second);
		list.add(third);
		WordEntry n = (WordEntry) list.getHead();
		for(int i=0; i<list.size(); i++) {
			switch(n.getLine()) {
				case 0:
				case 1:
					Assert.assertNull(n.getContext());
					break;
				case 10:
					Assert.assertEquals("hello world",n.getContext());
			}
		}
	}
	
	@Test
	public void testIsDuplicate() {
		WordEntry e1 = new WordEntry(1,"hello");
		WordEntry e2 = new WordEntry(1,"hello");
		Assert.assertTrue(e1.identical(e2));
	}
	
	@Test
	public void testIsNotDuplicate() {
		WordEntry e1 = new WordEntry(1,"hello");
		WordEntry e2 = new WordEntry(1,"world");
		Assert.assertFalse(e1.identical(e2));
	}
	
	@Test
	public void testAddDuplicate() {
		LittleLinkedList<WordEntry> tmp = new LittleLinkedList<WordEntry>();
		WordEntry e1 = new WordEntry(1,"hello");
		WordEntry e2 = new WordEntry(1,"hello");
		tmp.add(e1); tmp.add(e2);
		Assert.assertEquals(1, tmp.size());
	}
	
	/**
	 * Make sure we can add to a linkedlist in a reasonable (e.g. O(n)) time -
	 * test that nothing strange is going on in the LittleLinkedList/WordEntry
	 * implementation when adding lots of elements.
	 */
	@Test
	public void testPerformance() {
		for(int i=0; i<NUM_ITERATIONS; i++) {
			list.add(new WordEntry(i,"line #"+i));
		}
		int i=0;
		WordEntry n = list.getHead();
		do{
			Assert.assertEquals("line #"+i,n.getContext());
			i++;
		} while((n = (WordEntry) n.next()) != null);
	}
	
}
