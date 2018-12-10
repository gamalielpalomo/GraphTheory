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
    int incidenceMatrix[][];
    
    public PN(int[][] preMatrix, int[][] postMatrix){
        this.preMatrix = preMatrix;
        this.postMatrix = postMatrix;
        incidenceMatrix = buildIncidenceMatrix();
    }
    
    private void buildCoverGraph(){
        
        int []transitions;
        
        //Convention used for representing the cover graph is: Use the label "n" for the nodes followed by an id number. Example: node 0 = "n0".
        
        coverGraph = new Graph();
        Node n0 = new Node("n0",Globals.marking);
        coverGraph.addNode(n0, true); //Here we add the root node to the coverGraph.
        
        Node itNode;
        int id = 1;
        while( (itNode = coverGraph.getNodeType(NodeType.FRONTERA)) != null ){
            if( coverGraph.getDuplicatedNodeNotFrontier(itNode) != null )
                itNode.setType( NodeType.DUPLICADO );
            else{
                
                transitions = this.buildActiveTransitions(itNode.getMarking());
                if( transitions == null )
                    //This is a FRONTIER node in the cover graph.
                    itNode.setType(NodeType.FRONTERA);
                else{
                    //There are at least one transition that is active with this marking node.
                    itNode.setType(NodeType.EXPANDIDO);
                    for( int i = 0; i < transitions.length; i++ ){
                        if( transitions[i] == 1 ){
                            //A new cover graph node has to be created
                            int []newMarking = buildNextMarking( itNode.getMarking(), buildFiringVector(i) );
                            Node newMarkingNode = new Node( "n"+ id, newMarking);
                            coverGraph.addNode(newMarkingNode, false);
                            id++;
                            //A new transition itNode -> newMarkingNode is created.
                            Transition newTransition = itNode.addTransition(newMarkingNode, "t"+i);
                            newMarkingNode.addPreTransition(newTransition);
                        }
                    }
                }
            }
        }
        
    }//End of buildCoverGraph
    
    private int[] buildNextMarking(int []current, int []firingVector){
        //This method is used for creating a new marking node
        //current is the current marking vector of size preMatrix.length.
        //firing vector is the vector referencing the transition to be executed.
        int []result  = multiply(incidenceMatrix, firingVector);
        return result;
    }//End of buildNextMarking
    
    private int[] multiply( int[][] matrix, int[] vector ){
        //This method returns the result of multiplying a bidimensional matrix and a vector.
        int []result = new int[vector.length];
        for( int matrixRow = 0; matrixRow < matrix.length; matrixRow++){
            for( int matrixColumn = 0; matrixColumn < vector.length; matrixColumn++ )
                result[matrixRow] += matrix[matrixRow][matrixColumn] * vector[matrixColumn];
        }
        return result;
    }//End of multiply
    
    private int[] buildFiringVector(int column){
        int []result = new int[preMatrix[0].length];
        for( int i = 0; i < preMatrix[0].length; i++)
            result[i] = ( i == column )? 1:0;
        return result;
    }
    
    private int[] buildActiveTransitions(int[]marking){
        
        //This function builds the active transitions with the current marking
        //The length of vector has to be equal to the number of transitions in the system.
        //The active status is represented as a 1 for an active transition and as 0 for an inactive one.
        int []result = new int[preMatrix[0].length];
        for (int i = 0; i < preMatrix[0].length; i++){
            result[i] = isActiveTransition(marking, i)? 1:0;
        }       
        
        boolean atLeastOneActive = false;
        for(int i=0; i < preMatrix[0].length; i++){
            if(result[i] == 1) atLeastOneActive = true;
        }
        
        if(atLeastOneActive) return result;
        else return null;
        
    }  //End of buildActiveTranstitions
    
    private boolean isActiveTransition(int []marking, int transition){
        boolean result = true;
        int []transitionPre = getMatrixColumn(preMatrix, transition);//Pre for transition
        for(int i = 0; i<preMatrix.length; i++){
            if(marking[i]<transitionPre[i]){
                return false;
            }
        }
        return result;
    }//End of isActiveTransition
    
    //This method gets the transposed vector in a matrix for a determined column index.
    //Is useful for obtaining a pre for a determined transition in a PN.
    private int[] getMatrixColumn(int[][] matrix, int column){
        int [] result = new int[preMatrix.length];
        for (int i = 0; i < preMatrix.length; i++){
            result[i] = preMatrix[i][column];
        }
        return result;
    }//End of getMatrixColumn
    
    private int[][] buildIncidenceMatrix(){
        int [][] result = new int[preMatrix.length][preMatrix[0].length];
        //This method builds the PN incidence matrix based on its pre and post matrices.
        for( int row = 0; row < preMatrix.length; row++){
            for( int column = 0; column < preMatrix[0].length; column++)
                result[row][column] = postMatrix[row][column] - preMatrix[row][column];
        }
        coverGraph = null;
        return result;
    }//End of buildIncidenceMatrix.
    
}
