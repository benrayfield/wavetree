#wavetree
Minimalist alternative to byte arrays with log time for substring, concat, count 0/1 bits in subrange, and index of Nth 0/1 bit, or more generally an API for such immutable (or TODO option for some parts mutable) data wrappers.

Wavetree is an alternative to byte arrays. It exponentially speeds up the following ops by using AVL tree (which is automatically balanced), from linear to log: substring, concat, count 0 or 1 bits in a range, get index of Nth 0 or 1 bit. Its bit aligned, bigEndian, and can read and (TODO) write all primitive types (boolean, byte, short/char, int, float, long, double) regardless of alignment. Future versions will have an isStateless() func to extend all these ops to a shared 64 bit address space where some parts are mutable and other parts immutable, maybe with a read and write locking system on the mutable parts. The API is humanaicore.wavetree.bit.Bits and doesnt have to use AVL tree. It could wrap any data source, which could become part of AVL trees or used as just that node. Any less flexible data sources can (TODO) can on-the-fly copy to to AVL tree form when substring ops are used, instead of the primitive funcs. Wavetree was originally a tree of scalar time ranges and average amplitudes, used for sound evolution, which is now in the humanaicore.wavetree.scalar package. It worked so well that I extended it to bitstrings. You can understand it well through these 3 files: humanaicore.wavetree.bit.Bits then AvlBitstring then Fast0To16Bits.