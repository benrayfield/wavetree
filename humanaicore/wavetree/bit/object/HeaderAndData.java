/** Ben F Rayfield offers HumanAiCore opensource GNU LGPL */
package humanaicore.wavetree.bit.object;
import humanaicore.wavetree.bit.Bits;

public interface HeaderAndData{
	
	public Bits data();

	/** may be empty */
	public Bits header();
	
	public Bits headerThenData();

}
