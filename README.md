#wavetree
Minimalist alternative to byte arrays with log time for substring, concat, count 0/1 bits in subrange, and index of Nth 0/1 bit, or more generally an API for such immutable (or TODO option for some parts mutable) data wrappers.

Wavetree is an alternative to byte arrays. It exponentially speeds up the following ops by using AVL tree (which is automatically balanced), from linear to log: substring, concat, count 0 or 1 bits in a range, get index of Nth 0 or 1 bit. Its bit aligned, bigEndian, and can read and (TODO) write all primitive types (boolean, byte, short/char, int, float, long, double) regardless of alignment. Future versions will have an isStateless() func to extend all these ops to a shared 64 bit address space where some parts are mutable and other parts immutable, maybe with a read and write locking system on the mutable parts. The API is humanaicore.wavetree.bit.Bits and doesnt have to use AVL tree. It could wrap any data source, which could become part of AVL trees or used as just that node. Any less flexible data sources can (TODO) on-the-fly copy to AVL tree form when substring ops are used, instead of the primitive funcs. Wavetree was originally a tree of scalar time ranges and average amplitudes, used for sound evolution, which is now in the humanaicore.wavetree.scalar package. It worked so well that I extended it to bitstrings. You can understand it well through these 3 files: humanaicore.wavetree.bit.Bits then AvlBitstring then Fast0To16Bits.

As I wrote about very high memory use (looking for help to solve this) in https://www.reddit.com/r/compsci/comments/3y8dpe/wavetree_minimalist_alternative_to_byte_arrays/

It preallocates all bit strings up to size 16 as Fast0To16Bits leaf objects (also TODO the current code doesnt always use those when substring/concat). You could create a new class that just wrapped small parts of a byte array if you didnt care about substrings starting or ending inside it, but for its full power, you pay RATIO_OF_MEMORY_TO_DATA times the size. Just a guess. Thats why for large data I'm planning a byte array wrapper that would be 1 to 1 size and would only do this other stuff when needed. Its not a competition for the simplest uses of byte arrays. Its a competition for when theres lots of changes or you want to sum or search the number of 0/1 bits across a range.

If the 0/1 summing/search part was dropped, that would reduce each node by a long.

On the other hand, if the data has much duplication and you substring/concat to create it that way, it could be many times smaller.

RATIO_OF_MEMORY_TO_DATA is high because each node has size, oneBits, 2 pointers, height, and maxHeightDiff, and some extra nodes for AVL tree.

Wavetree has serious memory problems especially with AVL tree, but the exponential speedups still overcome that in the limit. I could use some help figuring this out. Here's some data:

All sizes measured in bits. startMemoryInBits=704643072 Subtracting this from wavetree memory use. This randBitsInAsBalancedATreeAsPossible does not pay extra for having the tree up to twice as deep, as usual in AVL, so it will actually use more.

wavetreeMem=1237319680 biggestBits=8388608 ratio=147

wavetreeMem=1262485504 biggestBits=16777216 ratio=75

wavetreeMem=1287651328 biggestBits=33554432 ratio=38

This time, random sizes so requires AVL rotation:

wavetreeMem=1270874112 biggestBits=2157624 ratio=589

wavetreeMem=1778384896 biggestBits=3994713 ratio=445

wavetreeMem=2063597568 biggestBits=3994713 ratio=516

wavetreeMem=2751463424 biggestBits=3994713 ratio=688

wavetreeMem=3628072960 biggestBits=3994713 ratio=908

wavetreeMem=3779067904 biggestBits=3994713 ratio=946

wavetreeMem=3892314112 biggestBits=3994713 ratio=974

wavetreeMem=5041553408 biggestBits=3994713 ratio=1262

wavetreeMem=5083496448 biggestBits=33554432 ratio=151
