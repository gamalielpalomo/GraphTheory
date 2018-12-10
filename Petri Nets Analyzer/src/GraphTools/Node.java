/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphTools;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        type = NodeType.FRONTERA;
        parent = null;
        postTransitions = new ArrayList();
        preTransitions = new ArrayList();
    }
    
    public Node(String id, int[] m){
        this(id);
        this.marking = m;
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
    public String getId(){ return id; }
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
                this.marking[i] = Node.W;
        }
    }
    
    public void setWs(){
        if( parent != null ){
            copyW( parent.marking ); //Copy all ws of the parent
            Node tmpParent = parent;
            while( tmpParent != null ){
                //if(isMkLargerThanMr(this, tmpParent)){
                if( isVector_nk_LE_nr( marking, tmpParent.marking ) ){
                    for( int i = 0; i < Array.getLength( marking ); i++ ){
                        if( marking[i] > tmpParent.marking[i] )
                            marking[i] = Node.W;
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
    
    public String getMarkString(){
        String markString = id+": ";
        
        for(int i=0; i < marking.length; i++){
            if( marking[i]== Node.W ){
                markString += "w ";
            }else{
                markString += String.valueOf( marking[i] ) + " ";
            }
        }
        if(this.getType()==NodeType.DUPLICADO){
                markString+="(D)";
            }else if(this.getType()==NodeType.EXPANDIDO){
                markString+="(E)";
            }else if(this.getType()==NodeType.TERMINAL){
                markString+="(T)";
            }

        return markString;
    }
    
    public List<Transition> getPreTransitions(){ return preTransitions; }
    public List<Transition> getPostTransitions(){ return postTransitions; }
    
    public int getMaxInMark(){
        int max = 0;
        
        for(int i = 0; i < Array.getLength(marking); i++){
            if(this.marking[i] > max)
                max = this.marking[i];
        }
        
        return max;
    }
    
    public int getSumMark(){
        int sum = 0;
        
        for(int i = 0; i < Array.getLength(marking); i++){
            if(this.marking[i] != Node.W)
                sum += this.marking[i];
            else
                return Node.W;
        }
        
        return sum;
    }
    
    public List<Node> getSucessorNodes(){
        List<Node> succNodes = new ArrayList<>();
                
        postTransitions.stream().forEach((transition) -> {
            succNodes.add(transition.getEnd());
        });
        return succNodes;
    }
}
