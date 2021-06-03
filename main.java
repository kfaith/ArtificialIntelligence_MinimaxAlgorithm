//Author: Kyle Faith

public class main {

    public static void main( String[] args ) {
        Board board = new Board();

        board.createTree(board.root, 1);
        System.out.println("\nMinimax Normal \n");
        long startTime1 = System.nanoTime();
        board.miniMax(1, 2, 20);
        long endTime1 = System.nanoTime();
        long timeTaken1 = (endTime1 - startTime1);
        System.out.println("Time Taken: "+ timeTaken1 + " nanoseconds");


        System.out.println("\nMinimax AlphaBeta Pruning \n");
        long startTime2 = System.nanoTime();
        board.alphabeta(board.root, -1,1,false);
        long endTime2 = System.nanoTime();
        long timeTaken2 = (endTime2 - startTime2);
        System.out.println("Time Taken: "+ timeTaken2 + " nanoseconds");

    }
}
