public class Perft {
    /**
     * Returns the total number of possible positions that can arise at a given depth.
     * @param board the board to find the positions on.
     * @param depth the depth in ply (half-moves) to go to.
     * @return number of possible positions after depth ply.
     */
    public static long Perft(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        Move[] moves = board.generateMoves(board.getTurn());
        for (Move move : moves) {
            if (move != null) {
                State stateBeforeMove = board.doMove(move);
                try {
                    nodes += Perft(board, depth - 1);
                    board.unDoMove(stateBeforeMove);
                } catch (Exception e) {
                    board.unDoMove(stateBeforeMove);
                    System.out.println("Error with move: " + move);
                    System.out.println("On board: " + FENUtils.getFEN(board));
                    board.doMove(move);
                    throw e;
                }
            }
        }
        return nodes;
    }

    /**
     * Returns the total number of possible positions that can arise at a given depth.
     * Also prints out how many positions there are for each move which is used for debugging.
     * @param board the board to find the positions on.
     * @param depth the depth in ply (half-moves) to go to.
     * @return number of possible positions after depth ply.
     */
    public static long PerftDivide(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        long count;
        Move[] moves = board.generateMoves(board.getTurn());
        for (Move move : moves) {
            if (move != null) {
                State stateBeforeMove = board.doMove(move);
                try {
                    count = Perft(board, depth - 1);
                    board.unDoMove(stateBeforeMove);
                    System.out.println(move + ": " + count);
                    nodes += count;
                } catch (Exception e) {
                    board.unDoMove(stateBeforeMove);
                    System.out.println("Error when doing move: " + move);
                    System.out.println("On board: " + FENUtils.getFEN(board));
                    board.doMove(move);
                    throw e;
                }
            }
        }
        return nodes;
    }

    /**
     * Returns the total number of possible positions that can arise at a given depth.
     * Allocates each possible CPU processor moves to carry out independently for parallelism.
     * Can also print out how many positions there are for each move which is used for debugging.
     * @param board the board to find the positions on.
     * @param depth the depth in ply (half-moves) to go to.
     * @param doDivide boolean for if move printing debugging will be done.
     * @return number of possible positions after depth ply.
     */
    public static long ThreadedPerft(Board board, int depth, boolean doDivide) {
        if (depth <= 2) {
            if (doDivide) {
                return PerftDivide(board, depth);
            } else {
                return Perft(board, depth);
            }
        }
        int numCPUCores = Runtime.getRuntime().availableProcessors();
        Move[] moves = board.generateMoves(board.getTurn());
        int numThreadMoves = (moves.length + numCPUCores - 1) / numCPUCores;
        final Move[][] threadMoves = new Move[numCPUCores][numThreadMoves];
        int threadMovesIndex;
        int index;
        Thread[] threads = new Thread[numCPUCores];
        long[] results = new long[numCPUCores];
        for (int threadIndex = 0; threadIndex < numCPUCores; threadIndex++) {
            index = threadIndex;
            threadMovesIndex = 0;
            while (index < moves.length) {
                threadMoves[threadIndex][threadMovesIndex] = moves[index];
                threadMovesIndex++;
                index += numCPUCores;
            }
            final int threadNum = threadIndex;
            threads[threadIndex] = new Thread(() -> {
                long nodes = 0;
                long count;
                for (Move move : threadMoves[threadNum]) {
                    if (move != null) {
                        Board boardCopy = board.copy();
                        Move moveCopy = move.clone(boardCopy);
                        boardCopy.doMove(moveCopy);
                        if (doDivide) {
                            count = Perft(boardCopy, depth - 1);
                            System.out.println(move + ": " + count);
                            nodes += count;
                        } else {
                            nodes += Perft(boardCopy, depth - 1);
                        }
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
