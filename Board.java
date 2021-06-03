import java.util.*;

public class Board {


    int piecesPlaced = 0;
    int piecesMax = 0;
    int piecesMin = 0;
    int nodeNumber1 = 0;
    boolean player1Turn = true;
    boolean player2Turn = false;
    int[][] blankBoard = {{0, 0, 0},
                          {0, 0, 0},
                          {0, 0, 0}};

    Node root = new Node(blankBoard);


    /**
     * formats any array inputted and outputs what it looks like as a board state.
     * @param array An array to be formatted.
     */
    public void formatBoard(int[][] array)
    {

        System.out.println(" ___________");
        System.out.println("| "+ array[0][0] + " | " + array[0][1] + " | " + array[0][2]+" |");
        System.out.println(" -----------");
        System.out.println("| "+ array[1][0] + " | " + array[1][1] + " | " + array[1][2]+" |");
        System.out.println(" -----------");
        System.out.println("| "+ array[2][0] + " | " + array[2][1] + " | " + array[2][2]+" |");
        System.out.println(" -----------");
    }


    /**
     * This method creates a tree for the input board, then prints the path to the best possible outcome for the max
     * player with max starting, and swapping between min and max's turns.
     * @param maxPlayer
     * @param minPlayer
     * @param depth
     */
    public void miniMax(int maxPlayer, int minPlayer, int depth)
    {
        root.setDepth(1);
        printPath(root);


    }

    /**
     * This method creates a tree from the base board, and then scores every single node once the tree starts to get
     * to the leaf nodes.
     * @param node The node which should have nodes created off of.
     * @param playerNum The player whose turn it is.
     */
    public void createTree(Node node, int playerNum)
    {

        player1Turn = false;
        player2Turn = false;
        List mylist = neighborsPlace(node.getState(), playerNum);

        //Creates the placement for the first 6 pieces, and creates all nodes in the tree(and their links)
        for(int i = 0; i < mylist.size(); i++) {
            Node current = new Node((int[][])mylist.get(i));
            int[][] array = current.getState();
            current.setParent(node);
            piecesPlaced = 0;
            piecesMax = 0;
            piecesMin = 0;
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {

                    if (array[x][y] != 0)
                        piecesPlaced++;
                    if(array[x][y] == 1)
                        piecesMax++;
                    if(array[x][y] == 2)
                        piecesMin++;
                }
            }

            current.setDepth(piecesPlaced+1);
            if(node.getDepth() < 7)
                node.addChild(current);
            //If the parent node has children leaf node, get the point values for the children and add to parent(for loop).
            if(node.getDepth() == 6)
            {
                current.setCost(checkPointValue(current.getState()));
                Node tempStorage = current;
                for(int a = 6; a > root.getDepth(); a--) {
                    scoreParent(current);
                    current = current.getParent();
                }
                current = tempStorage;
                continue;
            }
            //if there are less max pieces(or equal) on the board, make the next tree with max turn next
            if((piecesMax <= piecesMin) && current.getDepth() < 7)
                createTree(current,  1);
            //if there are less min pieces on the board, make the next tree with min turn next
            if((piecesMax > piecesMin) && current.getDepth() < 7)
                createTree(current,  2);
        }
        //End of the creation of the tree placing nodes.

        //Ideally if I would create the rest of the tree for the remaining moves(movement of pieces), but Im running out
        // of time on the assignment, and I already implemented the minimax algorithm so I feel I have completed a good
        // amount of what the assignment is supposed to teach us.
    }

    /**
     * This method simply sets the cost of the parents to the sum of their childrens scores.
     * @param node The node which should be scored.
     */
    public void scoreParent(Node node)
    {
        Node parent = node.getParent();

        parent.setCost(parent.getCost() +  node.getCost());

    }

    /**
     * This method checks the point value for the inputted node, if it is a win for columns, rows, or diagonals it
     * will return a +1 if player 1(max) wins, a 0 for a tie, and a -1 if player 2(min) wins.
     * @param node The node which should be checked for win.
     * @return The score.
     */
    public int checkPointValue(int[][] node)
    {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++)
        {
            if (node[row][0] == node[row][1] &&
                    node[row][1] == node[row][2])
            {
                if (node[row][0] == 1)
                    return +1;
                else if (node[row][0] == 2)
                    return -1;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++)
        {
            if (node[0][col] == node[1][col] &&
                    node[1][col] == node[2][col])
            {
                if (node[0][col] == 1)
                    return +1;

                else if (node[0][col] == 2)
                    return -1;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (node[0][0] == node[1][1] && node[1][1] == node[2][2])
        {
            if (node[0][0] == 1)
                return +1;
            else if (node[0][0] == 2)
                return -1;
        }

        if (node[0][2] == node[1][1] && node[1][1] == node[2][0])
        {
            if (node[0][2] == 1)
                return +1;
            else if (node[0][2] == 2)
                return -1;
        }

        // Else if none of them have won then return 0
        return 0;
    }


    /**
     * This method creates a list of neighbors to the inputted array, and determines which players turn it is based
     * on how many pieces have been placed on the current board.
     * @param array The array which is altered to find neighbors.
     * @param player The player whose turn it is.
     * @return The neighbors of the original array.
     */
    public List<int[][]> neighborsPlace(int[][] array, int player)
    {
        List neighborList = new ArrayList();


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[][] tempArray = new int[3][3];
                piecesPlaced = 0;
                //Fills the temp array with the normal array.
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        tempArray[x][y] = array[x][y];
                        if(tempArray[x][y] != 0)
                            piecesPlaced++;
                    }}
                if (tempArray[i][j] == 0) {
                    tempArray[i][j] = player;

                    neighborList.add(tempArray);
                }
            }
        }
        if(player == 2)
        {
            player1Turn = true;
        }
        if(player == 1)
        {
            player2Turn = true;
        }

        return neighborList;
    }

    /**
     * This method simply outputs the first 6 boards (which is the best possible path for the max player, if the min
     * player is playing perfectly).
     * @param root The root node which has a path created off of it.
     */
    public void printPath(Node root)
    {
        int bestNodeCost = 0;
        Node bestNode = null;
        ArrayList<Node> childList;

        System.out.println("Base Board:");
        formatBoard(root.getState());

        bestNodeCost = 0;
        childList = root.getChildren();
        for (int i = 0; i < childList.size(); i++) {
            if (bestNodeCost <= childList.get(i).getCost()) {
                bestNodeCost = childList.get(i).getCost();
                bestNode = childList.get(i);
            }
        }
        System.out.println("Max Move:");
        formatBoard(bestNode.getState());

            bestNodeCost = 0;
            childList = bestNode.getChildren();
            for (int i = 0; i < childList.size(); i++) {
                if (bestNodeCost >= childList.get(i).getCost()) {
                    bestNodeCost = childList.get(i).getCost();
                    bestNode = childList.get(i);
                }
            }
            System.out.println("Min Move:");
            formatBoard(bestNode.getState());

            bestNodeCost = 0;
            childList = bestNode.getChildren();
            for (int i = 0; i < childList.size(); i++) {
                if (bestNodeCost < childList.get(i).getCost()) {
                    bestNodeCost = childList.get(i).getCost();
                    bestNode = childList.get(i);
                }
            }
            System.out.println("Max Move:");
            formatBoard(bestNode.getState());


        bestNodeCost = 0;
        childList = bestNode.getChildren();
        for (int i = 0; i < childList.size(); i++) {
            if (bestNodeCost >= childList.get(i).getCost()) {
                bestNodeCost = childList.get(i).getCost();
                bestNode = childList.get(i);
            }
        }
        System.out.println("Min Move:");
        formatBoard(bestNode.getState());


        bestNodeCost = 0;
        childList = bestNode.getChildren();
        for (int i = 0; i < childList.size(); i++) {
            if (bestNodeCost <= childList.get(i).getCost()) {
                bestNodeCost = childList.get(i).getCost();
                bestNode = childList.get(i);
            }
        }
        System.out.println("Max Move:");
        formatBoard(bestNode.getState());

        bestNodeCost = 0;
        childList = bestNode.getChildren();
        for (int i = 0; i < childList.size(); i++) {
            if (bestNodeCost >= childList.get(i).getCost()) {
                bestNodeCost = childList.get(i).getCost();
                bestNode = childList.get(i);
            }
        }
        System.out.println("Min Move:");
        formatBoard(bestNode.getState());
    }


    /**
     * This method creates the alpha beta pruning method in which it will cut out more than half of the nodes of the
     * already existing search tree. I did not have enough time to implement the printing of the alphabeta version, so
     * the time taken for alphabeta may be slightly lower than it should be.
     * @param node The root node
     * @param alpha The alpha value, which should be 1
     * @param beta The beta value which should be -1
     * @param isMaxNode A boolean stating whether it is a max node or min node.
     * @return
     */
    public double alphabeta(Node node, double alpha, double beta, boolean isMaxNode)
    {
        double value;
        ArrayList<Node> childList = node.getChildren();

        //if its a leaf node
        if(node.getDepth() == 7)
        {
            return checkPointValue(node.getState());
        }
        //if its a max node
        if(isMaxNode)
        {
            value = Double.NEGATIVE_INFINITY;
            for(int i = 0; i < childList.size(); i++)
            {
                value = Math.max(value, alphabeta(childList.get(i), alpha, beta, false));
                alpha = Math.max(alpha,value);
                if(alpha >= beta)
                {
                    break;
                }
            }
        }
        //if its a min node
        else
        {
            value = Double.POSITIVE_INFINITY;
            for(int i = 0; i < childList.size(); i++)
            {
                value = Math.min(value, alphabeta(childList.get(i), alpha, beta, false));
                alpha = Math.min(beta,value);
                if(alpha <= beta)
                {
                    break;
                }
            }
        }
        return value;
    }

}
