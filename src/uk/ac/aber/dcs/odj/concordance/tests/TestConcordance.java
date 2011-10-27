package uk.ac.aber.dcs.odj.concordance.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.odj.concordance.Concordance;

/**
 * Creates a concordance, tests that it's initialized correctly, and then
 * runs through three documents, each with a word which will definitely appear
 * in that text. Asserts that the word count for the word we're looking at
 * per-document is the same as that reported by `grep -i word -c file`.
 * @author Owain Jones <odj@aber.ac.uk>
 *
 */
public class TestConcordance {
	private Concordance c;
	private String[] index;
	private String[] files;
	
	@Before
	public void setUp() {
		c = new Concordance();
		index = new String[]{"jesus","macbeth","workhouse"};
	}
	
	@Test
	public void testInit() {
		c.setIndex(index);
		c.generateIndex();
		Assert.assertEquals(3, c.size());
		for(String s : index) {
			Assert.assertTrue(c.containsKey(s));
		}
	}
	
	@Test
	public void testShakespeare() throws IOException {
		testInit();
		String file = new File("./resources/shakespeare.txt").getCanonicalPath();
		c.setFile(file);
		c.scan();
		Assert.assertEquals(283, c.get("macbeth").size());
	}
	
	@Test
	public void testBible() throws IOException {
		testInit();
		String file = new File("./resources/kingjames_bible.txt").getCanonicalPath();
		c.setFile(file);
		c.scan();
		Assert.assertEquals(973, c.get("jesus").size());
	}
	
	@Test
	public void testOliverTwist() throws IOException {
		testInit();
		String file = new File("./resources/oliver_twist.txt").getCanonicalPath();
		c.setFile(file);
		c.scan();
		Assert.assertEquals(43, c.get("workhouse").size());
	}
	
	@Test
	public void testCommonWords() throws IOException {
		String[] commons = new String[]{"and","or","the","that","is"};
		c.setIndex(commons);
		c.generateIndex();
		String file = new File("./resources/shakespeare.txt").getCanonicalPath();
		c.setFile(file);
		c.scan();
	}

}
