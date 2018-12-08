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
    
    public Node getNodeType(NodeType type){
        Node result = null;
        for (int i = 0; i <= this.nodes.size()-1; i++){
            if( this.nodes.get(i).getType() == type ){
                result = this.nodes.get(i);
                break;
            }
        }
        return result;
    }
    
    public Node getDuplicatedNodeNotFrontier(Node node){
        Node result = null;
        for ( int i = 0; i <= this.nodes.size() - 1; i++ ){
            if(this.nodes.get(i) != node && this.nodes.get(i).getType() != NodeType.FRONTERA && this.nodes.get(i).compareMarking(node.getMarking()))
                return this.nodes.get(i);
        }
        return result;
    }
    
}
