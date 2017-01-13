import java.util.Arrays;
import java.util.HashMap;


public class FibonacciHeap {
    Node maxNode;
    int totalNodes;

    static class Node {
         
         int frequency;

         Node parent;
         Node leftSibling;
         Node rightSibling;
         Node child;
         int degree;
         boolean childCut;

        Node(int frequency) {
           
            this.frequency= frequency;

            this.parent= null;
            this.leftSibling= this;
            this.rightSibling= this;
            this.child= null;
            this.degree= 0;
            this.childCut= false;
        }

        
    }

   
    //to create a Fibonacci Heap
    public FibonacciHeap() {
        this.maxNode= null;
        this.totalNodes = 0;
        
    }

    
    //to insert a node
    public Node insert(Node insertNode) {


        if (maxNode == null) { //in case there is no heap before, the first element is max it self
            maxNode= insertNode;
            totalNodes =1;
        } else {
            joinSiblings(insertNode,maxNode); 
            totalNodes++; //increase the node numbers by 1
            if (insertNode.frequency > maxNode.frequency) //change maxNode if the new node's frequency is greater
                maxNode= insertNode;
        }
        return insertNode;
    }

    //removes the node from the sibling's list, and points its left and right sibling to each other
    private void removeFromList(Node remNode) {
        if (remNode.rightSibling == remNode)
            return;
        remNode.rightSibling.leftSibling= remNode.leftSibling;
        remNode.leftSibling.rightSibling= remNode.rightSibling;
        remNode.rightSibling= remNode;
        remNode.leftSibling= remNode;
    }
    
    
    //to join the two lists of siblings
    private void joinSiblings(Node max, Node n) {
    	Node a = max.rightSibling;
    	Node b = n.leftSibling;
    	max.rightSibling = n;
    	n.leftSibling = max;
    	a.leftSibling = b;
    	b.rightSibling = a;
    }

    
    //function to remove the maxNode
    public Node removeMax() {
        if (maxNode == null)
            return null;
        if (maxNode.child != null) 
        {
            Node temp= maxNode.child;
            while (temp.parent != null) 
            {
                temp.parent= null;
                temp= temp.rightSibling;
            }
            // we need to add children of maxNode to the first list (which has the root)
            joinSiblings(temp, maxNode);
        }
        // remove max from root list
        Node prevMax= maxNode;
        if (maxNode.rightSibling == maxNode) {
            maxNode= null;
            totalNodes--;
        } else {
            maxNode= maxNode.rightSibling;
            removeFromList(prevMax);
            pairwiseCombine();
            totalNodes--;
        }
        resetNode(prevMax);//to make this removed max an isolated node
        return prevMax;
    }

    //resets the values of the removed Max node
    void resetNode(Node n) { 
    	n.parent= null;
        n.leftSibling= n;
        n.rightSibling= n;
        n.child= null;
        n.degree= 0;
        n.childCut= false;
    }
    
    // pairwise Combines heaps of same degree...melding essentially
    private void pairwiseCombine() { 
        Node[] listNewRoots= new Node[totalNodes];
        Node node= maxNode;
        Node start= maxNode;
        do {
            Node x= node;
            int currentDegree= node.degree;
            while (listNewRoots[currentDegree] != null) {
                Node y= listNewRoots[currentDegree];
                if (y.frequency > x.frequency) {
                    Node temp= x;
                    x= y;
                    y= temp;
                }
                if (y == start) {
                    start= start.rightSibling;
                }
                if (y == node) {
                    node= node.leftSibling;
                }
                join(y, x); //to link the new nodes
                listNewRoots[currentDegree++]= null;
            }
            listNewRoots[currentDegree]= x;
            node= node.rightSibling;
        } while (node != start);
        maxNode= null;
        for (int i= 0; i < listNewRoots.length; i++)
            if (listNewRoots[i] != null) {
                if ( (maxNode == null)|| (listNewRoots[i].frequency > maxNode.frequency) )
                    maxNode= listNewRoots[i];
            }
    }

    // links y under x
    
    private void join(Node y, Node x) {//report
        removeFromList(y);
        y.parent= x;
        if (x.child == null)
            x.child= y;
        else
        	joinSiblings(x.child, y);
        x.degree++;
        y.childCut= false;
        
    }

    
    //when the value of one key has to be increased
    public void increaseKey(Node node, int frequency) {
        node.frequency= frequency;
        Node parent= node.parent;
        //cut and cascadingCut take place after increaseKey
        if ( (parent != null) && (node.frequency >parent.frequency) ) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.frequency > maxNode.frequency) //change maxNode accordingly
            maxNode= node;

    }

    // cut function to remove x from below y ( x from y's children )
    private void cut(Node x, Node y) { 
        if (y.child == x)
            y.child= x.rightSibling;
        if (y.child == x)
            y.child= null;

        y.degree--; //degree decreases as the child got removed
        removeFromList(x);
        joinSiblings(x, maxNode);
        x.parent= null;
        x.childCut= false;

    }

    //cascading cut is to cut out of its sibling list in a remove or increase key operation
    //it follows path from parent of the Node to the root
    private void cascadingCut(Node y) {
        Node z= y.parent;
        if (z != null) {
            if (!y.childCut) {
                y.childCut= true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

}