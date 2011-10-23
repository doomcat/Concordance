package uk.ac.aber.dcs.odj;

import java.io.IOException;
import java.util.Enumeration;

public class Run {
	static final String[] index = {
		"Jesus"
	};
	
	public static void main(String[] args) {
		final long startTime = System.nanoTime();
		final long endTime;
		try {
			Concordance concordance = new Concordance("./kingjames_bible.txt",
					index);
			concordance.scan();
			for(String key : concordance.keySet()) {
				System.out.println(key.toUpperCase());
				LittleLinkedList list = concordance.get(key);
				System.out.println("# entries:\t"+list.size());
				WordEntry entry = (WordEntry) list.getHead();
				while(entry != null) {
					System.out.println("----");
					System.out.println("\tLine:\t\t"+entry.getLine());
					System.out.println("\tContext:\t..."+entry.getContext()+"...");
					entry = (WordEntry) entry.next();
				}
				System.out.println();
			}
		} catch (IOException e) {
			System.out.println("File loading error:");
			e.printStackTrace();
		} finally {
			endTime = System.nanoTime();
		}
		System.out.println("Time taken: "+(endTime-startTime));
	}

}
