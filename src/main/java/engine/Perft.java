import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Perft {
    /**
     * Returns the total number of possible positions that can arise at a given depth.
     * @param position the position to find the positions on.
     * @param moveGen the move generator object.
     * @param transpositionTable the transposition table.
     * @param depth the depth in ply (half-moves) to go to.
     * @return number of possible positions after depth ply.
     */
    public static long Perft(Position position, MoveGenerator moveGen, PerftTT transpositionTable, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        long nodeCount;
        IntArrayList moves = moveGen.generateLegalMoves(position);
        for (int move : moves) {
            if (move != MoveFlags.NO_MOVE) {
                State stateBeforeMove = position.doMove(move);
                long ttEntry = transpositionTable.lookUp(position.getZobristKey(), depth - 1);
                if (ttEntry != ChessConstants.NO_TRANSPOSITION) {
                    nodeCount = ttEntry;
                } else {
                    nodeCount = Perft(position, moveGen, transpositionTable, depth - 1);
                    transpositionTable.add(position.getZobristKey(), depth - 1, nodeCount);
                }
                nodes += nodeCount;
                position.unDoMove(stateBeforeMove);
            }
        }
        return nodes;
    }

    /**
     * Returns the total number of possible positions that can arise at a given depth.
     * Also prints out how many positions there are for each move which is used for debugging.
     * @param position the position to find the positions on.
     * @param moveGen the move generator object.
     * @param transpositionTable the transposition table.
     * @param depth the depth in ply (half-moves) to go to.
     * @return number of possible positions after depth ply.
     */
    public static long PerftDivide(Position position, MoveGenerator moveGen, PerftTT transpositionTable, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        long nodeCount;
        IntArrayList moves = moveGen.generateLegalMoves(position);
        for (int move : moves) {
            if (move != MoveFlags.NO_MOVE) {
                State stateBeforeMove = position.doMove(move);
                long ttEntry = transpositionTable.lookUp(position.getZobristKey(), depth - 1);
                if (ttEntry != ChessConstants.NO_TRANSPOSITION) {
                    nodeCount = ttEntry;
                } else {
                    nodeCount = Perft(position, moveGen, transpositionTable, depth - 1);
                    transpositionTable.add(position.getZobristKey(), depth - 1, nodeCount);
                }
                nodes += nodeCount;
                System.out.println(UCIUtils.moveToUCIString(move) + ": " + nodeCount);
                position.unDoMove(stateBeforeMove);
            }
        }
        return nodes;
    }

    /**
     * Returns the total number of possible positions that can arise at a given depth.
     * Allocates each possible CPU processor moves to carry out independently for parallelism.
     * Can also print out how many positions there are for each move which is used for debugging.
     * @param position the position to find the positions on.
     * @param transpositionTable the transposition table.
     * @param depth the depth in ply (half-moves) to go to.
     * @param doDivide boolean for if move printing debugging will be done.
     * @return number of possible positions after depth ply.
     */
    public static long ThreadedPerft(Position position, PerftTT transpositionTable, int depth, boolean doDivide) {
        if (depth <= 2) {
            if (doDivide) {
                return PerftDivide(position, new MoveGenerator(), transpositionTable, depth);
            } else {
                return Perft(position, new MoveGenerator(), transpositionTable, depth);
            }
        }
        int numCPUCores = Runtime.getRuntime().availableProcessors();
        MoveGenerator moveGen = new MoveGenerator();
        IntArrayList moves = moveGen.generateLegalMoves(position);
        int numThreadMoves = (moves.size() + numCPUCores - 1) / numCPUCores;
        final int[][] threadMoves = new int[numCPUCores][numThreadMoves];
        int threadMovesIndex;
        int index;
        Thread[] threads = new Thread[numCPUCores];
        long[] results = new long[numCPUCores];
        for (int threadIndex = 0; threadIndex < numCPUCores; threadIndex++) {
            index = threadIndex;
            threadMovesIndex = 0;
            while (index < moves.size()) {
                threadMoves[threadIndex][threadMovesIndex] = moves.getInt(index);
                threadMovesIndex++;
                index += numCPUCores;
            }
            final int threadNum = threadIndex;
            threads[threadIndex] = new Thread(() -> {
                long nodes = 0;
                long nodeCount;
                long ttEntry;
                for (int move : threadMoves[threadNum]) {
                    if (move != MoveFlags.NO_MOVE) {
                        Position positionCopy = position.copy();
                        positionCopy.doMove(move);
                        ttEntry = transpositionTable.lookUp(positionCopy.getZobristKey(), depth - 1);
                        if (ttEntry != ChessConstants.NO_TRANSPOSITION) {
                            nodeCount = ttEntry;
                        } else {
                            nodeCount = Perft(positionCopy, new MoveGenerator(), transpositionTable, depth - 1);
                        }
                        if (doDivide) {
                            System.out.println(UCIUtils.moveToUCIString(move) + ": " + nodeCount);
                        }
                        nodes += nodeCount;
                    }
                }
                results[threadNum] = nodes;
            });
            threads[threadIndex].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread error");
                System.out.println(e);
            }
        }
        long total = 0;
        for (long result : results) {
            total += result;
        }
        return total;
    }
}
