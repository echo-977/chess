public class Perft {
    public static long Perft(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        Move[] moves = board.generateMoves(board.getTurn());
        for (Move move : moves) {
            if (move != null) {
                try {
                    Board boardCopy = board.copy();
                    Move moveCopy = move.clone(boardCopy);
                    boardCopy.doMove(moveCopy);
                    nodes += Perft(boardCopy, depth - 1);
                } catch (Exception e) {
                    System.out.println("Error with move: " + move);
                    throw e;
                }
            }
        }
        return nodes;
    }

    public static long PerftDivide(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        long count;
        Move[] moves = board.generateMoves(board.getTurn());
        for (Move move : moves) {
            if (move != null) {
                Board boardCopy = board.copy();
                Move moveCopy = move.clone(boardCopy);
                boardCopy.doMove(moveCopy);
                try {
                    count = Perft(boardCopy, depth - 1);
                    System.out.println(move + ": " + count);
                    nodes += count;
                } catch (Exception e) {
                    System.out.println(FENUtils.getFEN(boardCopy));
                    System.out.println(moveCopy);
                    throw e;
                }
            }
        }
        return nodes;
    }

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
