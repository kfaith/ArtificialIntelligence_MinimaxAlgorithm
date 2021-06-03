import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Stack;

public class Node {

    private static Board stateObj = new Board();
    private int[][] state;
    private ArrayList<Node> children;
    private Node parent;
    private int depth;
    private int cost;

    public Node(int[][] start)
    {

        this.state = start;
        children = new ArrayList<>();
    }

    public int[][] getState()
    {
        return state;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setCost( int guess)
    {
        this.cost =  guess;
    }

    public int getCost()
    {
        return cost;
    }


    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Node getParent() {
        return parent;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public int getDepth(){
        return depth;
    }

    //    Prints game state
    public static void printPath(Node goalNode, Node root) {

        Stack<Node> solutionStack = new Stack<>();
        Stack<Node> tempStack = new Stack<>();
        solutionStack.push(goalNode);
        while (!(Arrays.deepEquals(goalNode.getState(),root.getState()))) {
            solutionStack.push(goalNode.getParent());
            goalNode = goalNode.getParent();
        }
        int size = solutionStack.size()-1;
        //This flips the stack over
        for(int i = 0; i <= size; i++)
        {
            tempStack.push(solutionStack.pop());
        }

        for (int i = 0; i <= size; i++) {
            System.out.println("Step " + (i+1));
//            stateObj.formatState(tempStack.get(i).getState());
        }

    }
}
