package uk.ac.aber.dcs.odj;

import java.io.IOException;
import java.util.Enumeration;

public class Run {
	static final String[] index = {
		"and"
	};
	
	public static void main(String[] args) {
		final long instTime, scanTime, iterTime;
		final long endInst, endScan, endIter;
		final long startFreeMem = Runtime.getRuntime().freeMemory(), endFreeMem;
		try {
			instTime = System.nanoTime();
			Concordance concordance = new Concordance("./kingjames_bible.txt",
					index);
			endInst = System.nanoTime()-instTime;
			
			scanTime = System.nanoTime();
			concordance.scan();
			endScan = System.nanoTime()-scanTime;
			
			iterTime = System.nanoTime();
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
			endIter = System.nanoTime()-iterTime;
			
			endFreeMem = Runtime.getRuntime().freeMemory();
			System.out.println("INST: "+endInst+", SCAN: "+endScan+", ITER: "+endIter);
			System.out.println("SMEM: "+startFreeMem+", EMEM: "+endFreeMem);
		} catch (IOException e) {
			System.out.println("File loading error:");
			e.printStackTrace();
		}
	}

}
