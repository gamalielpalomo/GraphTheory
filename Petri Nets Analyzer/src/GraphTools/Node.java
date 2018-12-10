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
    private Node parent;
    private List<Transition> postTransitions;
    private List<Transition> preTransitions;
    public static final int W = Integer.MAX_VALUE;
    private int[] mark;
    
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
    
    public void setType(NodeType type){ this.type = type; }
    public void setParent(Node newParent){ parent = newParent; }
    public void addPreTransition(Transition newTransition){
        preTransitions.add(newTransition);
    }
    public Transition addTransition(Node targetNode, String transitionLabel){
        Transition result = new Transition(transitionLabel);
        result.setEnd(targetNode);
        postTransitions.add(result);
        targetNode.setParent(this);
        return result;
    }
    public NodeType getType(){ return type; }
    public int[] getMarking(){ return marking; }
    public boolean compareMarking(int[] marking){
        boolean result = false;
        if(Arrays.equals(this.marking, marking))
            result = true;
        return result;
    }
    public Node getParent(){ return parent; }
    
    public void copyW(int[] m){
        for(int i = 0; i < m.length; i++){
            if(m[i] == Node.W)
                this.mark[i] = Node.W;
        }
    }
    
    public void setWs(){
        if( parent != null ){
            copyW( parent.mark ); //Copy all ws of the parent
            Node tmpParent = parent;
            while( tmpParent != null ){
                //if(isMkLargerThanMr(this, tmpParent)){
                if( isVector_nk_LE_nr( mark, tmpParent.mark ) ){
                    for( int i = 0; i < Array.getLength( mark ); i++ ){
                        if( mark[i] > tmpParent.mark[i] )
                            mark[i] = Node.W;
                    }
                }
                tmpParent = tmpParent.getParent();
            }
        }
    }
    
    public boolean isVector_nk_LE_nr(int[] mk, int[] mr){

        boolean is_nk_LE_nr= true;
        for ( int i=0; i < mk.length; i++ ){
            if ( mk[i] < mr[i] ){
                return false;
            }           
        }    
        return is_nk_LE_nr;
    }
    
}
