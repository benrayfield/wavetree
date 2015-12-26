package humanaicore.wavetree.bit;

import java.util.Random;

public class TestUseLotsOfMemory{
	
	static Runtime r;
	
	public static void main(String[] args){
		r = Runtime.getRuntime();
		System.gc();
		long startMemoryInBits = r.totalMemory()*8;
		System.out.println("All sizes measured in bits.");
		System.out.println("startMemoryInBits="+startMemoryInBits+" Subtracting this from wavetree memory use.");
		long size = 1<<25;
		//boolean unbalancedAndRandomSplitSize = true;
		boolean unbalancedAndRandomSplitSize = false;
		if(!unbalancedAndRandomSplitSize) System.out.println("This randBitsInAsBalancedATreeAsPossible does not pay extra for having the tree up to twice as deep, as usual in AVL, so it will actually use more.");
		Bits b = randBitsInAsBalancedATreeAsPossible(unbalancedAndRandomSplitSize, size, new Random());
		gcAndPrint();
	}
	
	static long cyclesSinceGC = 0;
	
	static long startMemoryInBits;
	
	static long biggestBits = 1;

	static Bits randBitsInAsBalancedATreeAsPossible(boolean unbalancedAndRandomSplitSize, long size, Random rand){
		if(size <= 16) return Fast0To16Bits.get((int)size, rand.nextInt(1<<(int)size));
		long aSize;
		if(unbalancedAndRandomSplitSize){
			aSize = 1 + size/30 + Math.abs(rand.nextLong())%(size/10);
		}else{
			aSize = size/2;
		}
		Bits a = randBitsInAsBalancedATreeAsPossible(unbalancedAndRandomSplitSize, aSize, rand);
		Bits b = randBitsInAsBalancedATreeAsPossible(unbalancedAndRandomSplitSize, size-aSize, rand);
		Bits cat = a.cat(b);
		biggestBits = Math.max(biggestBits, cat.siz());
		cyclesSinceGC++;
		if((cyclesSinceGC%1000000)==0) gcAndPrint();
		return cat;
	}
	
	static void gcAndPrint(){
		sleep(1);
		System.gc();
		sleep(1);
		long totalBitsOfMemoryNow = r.totalMemory()*8;
		long bitsOfMemoryNow = totalBitsOfMemoryNow-startMemoryInBits;
		double ratio = (double)bitsOfMemoryNow/biggestBits;
		System.err.println("wavetreeMem="+bitsOfMemoryNow+" biggestBits="+biggestBits+" ratio="+(long)ratio);
	}
	
	static void sleep(double seconds){
		long millis = (long)(seconds*1000); //TODO use other sleep function that also uses nanoseconds
		try{
			Thread.sleep(millis);
		}catch(InterruptedException e){}
	}

}