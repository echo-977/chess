import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

public class PerftTT {
    private final AtomicLongArray keys;
    private final AtomicIntegerArray depths;
    private final AtomicLongArray nodeCounts;
    private final Object[] locks;
    int mask;

    /**
     * Constructs a transposition table for perft with the given size.
     * The table size must be a power of two, as the index is computed by bitwise ANDing the hash with one less than size
     * @param size the number of entries in the table.
     */
    public PerftTT(int size) {
        mask = size - 1;
        keys = new AtomicLongArray(size);
        depths = new AtomicIntegerArray(size);
        nodeCounts = new AtomicLongArray(size);
        locks = new Object[size];
        for (int i = 0; i < size; i++) {
            locks[i] = new Object();
        }
    }

    /**
     * Add a new entry to the transposition table.
     * @param key the zobrist key for the position.
     * @param depth the depth at which the node count exists for.
     * @param nodeCount the number of nodes at the given depth.
     */
    public void add(long key, int depth, long nodeCount) {
        int index = (int) (key & mask);
        synchronized (locks[index]) {
            keys.set(index, key);
            depths.set(index, depth);
            nodeCounts.set(index, nodeCount);
        }
    }

    /**
     * Searches for if an entry exists in the given position with the given depth.
     * @param key the zobrist key for the position.
     * @param depth the depth at which the node count exists for.
     * @return the node count if they key and depth both match, otherwise -1.
     */
    public long lookUp(long key, int depth) {
        int index = (int) (key & mask);
        synchronized (locks[index]) {
            if (keys.get(index) == key && depths.get(index) == depth) {
                return nodeCounts.get(index);
            }
            return ChessConstants.NO_TRANSPOSITION;
        }
    }
}
