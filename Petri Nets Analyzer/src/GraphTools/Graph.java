/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphTools;

import java.util.ArrayList;
import java.util.List;
import Global.Globals;

/**
 *
 * @author Tania Rodriguez
 */
public class Graph {
    
    private List<Node> nodes;
    private Node root;
    
    public Graph(){
        nodes = new ArrayList<>();
        root = null;
    }
    
    public void addNode(Node newNode, boolean isRoot){
        this.nodes.add(newNode);
        if (isRoot)
            this.root = newNode;
    }
    
    public List<Node> getNodes(){ return nodes; }
    
    /*public Node getNodeType(NodeType type){
        Node result = null;
        for (int i = 0; i < nodes.size(); i++){
            if( nodes.get(i).getType() == type ){
                return this.nodes.get(i);
            }
        }
        return result;
    }*/
    
    public Node getNodeType(NodeType type){
        
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i).getType() == type)
                return this.nodes.get(i);
        }
        
        return null;
    }
    
    /*public Node getDuplicatedNodeNotFrontier(Node node){
        Node result = null;
        for ( int i = 0; i <= this.nodes.size() - 1; i++ ){
            if(this.nodes.get(i) != node && this.nodes.get(i).getType() != NodeType.FRONTERA && this.nodes.get(i).compareMarking(node.getMarking()))
                return this.nodes.get(i);
        }
        return result;
    }*/
    
    public Node getDuplicatedNodeNotFrontier(Node node){
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i) != node && 
               this.nodes.get(i).getType() != NodeType.FRONTERA &&
               this.nodes.get(i).compareMarking(node.getMarking()))
                return this.nodes.get(i);
        }
        
        return null;
    }
    
    public Node getDuplicateNodeExpandido(Node node){
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i) != node && 
               this.nodes.get(i).getType() == NodeType.EXPANDIDO &&
               this.nodes.get(i).compareMarking(node.getMarking()))
                return this.nodes.get(i);
        }
        
        return null;
    }
    
    public boolean removeNode(Node node){
        
        for (int i=0; i < nodes.size(); i++){
            //If Id strings match.
            if(nodes.get(i).getId().compareTo(node.getId())== 0){
                nodes.remove(i);
                return true;
            }                
        }
        return false;
    }
    
    public int getMaxBound(){
        int max = 0, maxAux;
 
        for (Node node : this.nodes) {
            maxAux = node.getMaxInMark();
            if (maxAux > max) {
                max =  maxAux;
            }
        }
        
        return max;
    }
    
    public boolean hasTerminalNodes(){
         for (Node node : this.nodes) {
             if(node.getType() == NodeType.TERMINAL){
                 return true;
             }
         }
         
         return false;
    }
    
    public boolean isEstrictlyConservative(){
        int sum, sumAnt = 0;
 
        if(this.nodes.size() > 0){
            sumAnt = this.nodes.get(0).getSumMark();
        }
        
        for (Node node : this.nodes) {
            sum = node.getSumMark();
            if (sum == Node.W) {
               return false;
            }
            if(sum != sumAnt){
               return false;
            }
        }
        
        return true;
    }
    
}
