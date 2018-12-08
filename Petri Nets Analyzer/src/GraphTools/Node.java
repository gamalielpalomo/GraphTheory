/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphTools;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Arrays;
/**
 *
 * @author Tania Rodriguez
 */
public class Node {
    
    private String id;
    private NodeType type;
    private int[] marking;
    private List<Transition> postTransitions;
    private List<Transition> preTransitions;
    
    public int index;
    public int lowlink;
    public boolean visited;
    public boolean onStack;
    
    public Node(){
        index = Integer.MAX_VALUE;
        lowlink = Integer.MAX_VALUE;
        visited = false;
        onStack = false;
    }
    
    public Node(String id){
        this();
        this.id = id;
    }
    
    public Node(String id, int[] marking){
        this(id);
        this.marking = marking;
    }
    
    public void setType(NodeType type)
    
    public NodeType getType(){ return type; }
    public int[] getMarking(){ return marking; }
    public boolean compareMarking(int[] marking){
        boolean result = false;
        if(Arrays.equals(this.marking, marking))
            result = true;
        return result;
    }
}
