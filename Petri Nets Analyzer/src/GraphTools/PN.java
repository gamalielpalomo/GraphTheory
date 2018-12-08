/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphTools;
import Global.Globals;
/**
 *
 * @author Tania Rodriguez
 */
public class PN {
    
    private Graph coverGraph;
    
    int preMatrix[][];
    int postMatrix[][];
    
    public PN(int[][] preMatrix, int[][] postMatrix){
        this.preMatrix = preMatrix;
        this.postMatrix = postMatrix;
    }
    
    private void buildCoverGraph(){
        
        //Convention used for representing the cover graph is: Use the label "n" for the nodes followed by an id number. Example: node 0 = "n0".
        
        coverGraph = new Graph();
        Node n0 = new Node("n0",Globals.marking);
        coverGraph.addNode(n0, true); //Here we add the root node to the coverGraph.
        
        Node itNode;
        while( (itNode = coverGraph.getNodeType(NodeType.FRONTERA)) != null ){
            if(coverGraph.getDuplicatedNodeNotFrontier(itNode) != null)
                itNode.setType( NodeType.DUPLICADO );
        }
        
    }//End of buildCoverGraph
    
}
