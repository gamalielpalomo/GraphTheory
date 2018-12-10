/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphTools;
import Global.Globals;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Tania Rodriguez
 */
public class PN {
    
    private Graph coverGraph;
    private Graph tarjanCG;
    
    private int[][] preMatrix;
    private int[][] postMatrix;
    private int[][] incidenceMatrix;
    private int[] marking;
    
    public PN(int[][] preMatrix, int[][] postMatrix, int[] marking){
        this.preMatrix = preMatrix;
        this.postMatrix = postMatrix;
        incidenceMatrix = buildIncidenceMatrix();
        this.marking = marking;
        coverGraph = null;
    }
    
    public void buildCoverGraph(){
        
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
                            newMarkingNode.setWs();
                        }
                    }
                }
            }
        }
        
    }//End of buildCoverGraph
    
        /**
     * Computes Coverage Graph for Tarjan Algorithm Processing.
     */
    public void buildTarjanCoverGraph(){
        tarjanCG = new Graph();
        int id = 0, j;
        
        Node nz = new Node("n" + String.valueOf(id), marking);
        tarjanCG.addNode(nz, true);
        id++;
        Node nk, dupNode;
        int transitions[];
        int mz[];
        
        
        while((nk = tarjanCG.getNodeType(NodeType.FRONTERA)) != null){
            //Verificar que no sea duplicado
            if(tarjanCG.getDuplicatedNodeNotFrontier(nk) != null){
                nk.setType(NodeType.DUPLICADO);                                
            }
            else{ //el nodo no es duplicado
                transitions = this.buildActiveTransitions(nk.getMarking()); //buscar transiciones habilitadas
            
                if(transitions == null){ //no hay transiciones habilitadas para el nodo
                    nk.setType(NodeType.TERMINAL);
                }
                else{                   //hay transiciones 
                    nk.setType(NodeType.EXPANDIDO);
                    for(j = 0; j < Array.getLength(transitions); j++){
                        if(transitions[j] == 1){
                            //Se crea el nodo nz
                            mz = this.buildNextMarking(nk.getMarking(), buildFiringVector(j));
                            nz = new Node("n" + String.valueOf(id), mz);// se crea como nodo frontera
                            tarjanCG.addNode(nz, false);
                            id++;
                            //Se crea transicion para nk --> nz
                            Transition t = nk.addTransition(nz, "t"+ String.valueOf(j+1));
                            //Buscar si no tiene Ws
                            nz.setWs();                           
                            
                            /*dupNode = tarjanCG.getDuplicateNodeNotFrontera(nz);
                            
                            if(dupNode != null){                                
                                t.redirectTransitionTo(dupNode);
                                if(!tarjanCG.removeNode(nz))
                                    System.out.print("Error: Could not remove node with Id:" + nz.getId());
                                else
                                    System.out.print("Seccessfully remove node with Id:" + nz.getId());
                                dupNode.setType(NodeType.DUPLICADO);
                            } */              
                        }
                    }
                }
            }
            
        }
        connectTarjanDuplicatesCoverGraph();
    }
    private void connectTarjanDuplicatesCoverGraph(){
        Node dupNode;        
        Node parentNode;
        Transition redirTrans;        
        List<Node> allNodes;
        List<Node> duplicateNodes = new ArrayList<>();
        
        if(tarjanCG== null){            
            this.buildTarjanCoverGraph();            
        }        
        allNodes = tarjanCG.getNodes();
        //Get duplicate Nodes only
        
        for(Node myNode : allNodes){ 
            if(myNode.getType()==NodeType.DUPLICADO)
                duplicateNodes.add(myNode);
        }
        
        for(Node myNode : duplicateNodes){   
            dupNode = tarjanCG.getDuplicateNodeExpandido(myNode);
            //Find pre Transition that connect to myNode                    
            parentNode = myNode.getParent();
            for (int j=0; j< parentNode.getPostTransitions().size(); j++){
                //Identify Transition that connects to myNode at its end
                redirTrans = parentNode.getPostTransitions().get(j);
                if (myNode.getId().compareTo(redirTrans.getEnd().getId())==0){
                    redirTrans.setTransitionTo(dupNode); //Redirects transition to matching node                    
                    tarjanCG.removeNode(myNode);
                }
            }

            
        }   
    }
    
    private int[] buildNextMarking(int []current, int []firingVector){
        //This method is used for creating a new marking node
        //current is the current marking vector of size preMatrix.length.
        //firing vector is the vector referencing the transition to be executed.
        int []multResult  = multiply(incidenceMatrix, firingVector);
        int []result = sumVectors(current, multResult);
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
    
    private int[] sumVectors(int[] vector1, int[] vector2){
        int []result = new int[vector1.length];
        for (int i = 0; i < vector1.length; i++)
            result[i] = vector1[i] + vector2[i];
        return result;
    }
    
    private int[] buildFiringVector(int column){
        int []result = new int[preMatrix[0].length];
        for( int i = 0; i < preMatrix[0].length; i++)
            result[i] = ( i == column )? 1:0;
        return result;
    }
    
    private int[] buildActiveTransitions(int[]m){
        
        //This function builds the active transitions with the current marking
        //The length of vector has to be equal to the number of transitions in the system.
        //The active status is represented as a 1 for an active transition and as 0 for an inactive one.
        int []result = new int[preMatrix[0].length];
        for (int i = 0; i < preMatrix[0].length; i++){
            result[i] = isActiveTransition(m, i)? 1:0;
        }       
        
        boolean atLeastOneActive = false;
        for(int i=0; i < preMatrix[0].length; i++){
            if(result[i] == 1) atLeastOneActive = true;
        }
        
        if(atLeastOneActive) return result;
        else return null;
        
    }  //End of buildActiveTranstitions
    
    private boolean isActiveTransition(int []m, int transition){
        boolean result = true;
        int []transitionPre = getMatrixColumn(preMatrix, transition);//Pre for transition
        for(int i = 0; i<preMatrix.length; i++){
            if(m[i]<transitionPre[i]){
                return false;
            }
        }
        return result;
    }//End of isActiveTransition
    
    public int[][] getPreMatrix(){ return preMatrix; }
    public int[][] getPostMatrix(){ return postMatrix; }
    public int[][] getIncidenceMatrix(){ return incidenceMatrix; }
    public int[] getMarkingVector(){ return marking; }
    public Graph getCoverGraph(){ return coverGraph; }
    public Graph getTarjanCoverGraph(){ return tarjanCG; }
    
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
    
    // Determinacion de propiedades de RP .......... //    
    public boolean isPNBounded(){
        //Ask the graph whether it contains a w in its markings.

        int maxBound;
        if(this.coverGraph != null){
            return (this.coverGraph.getMaxBound() != Node.W);
        }
        return false;
    }      
    // Determinacion de propiedades de RP .......... //    
    public int getMaxBoundValue(){
    
        if(this.coverGraph != null){
            return this.coverGraph.getMaxBound();
        }
        //Ask the graph whether it contains a w in its markings.
        return 0;
    } 
    public boolean isPNBlockageFree(){        
        if(this.coverGraph != null){
            return !(this.coverGraph.hasTerminalNodes());
        }
        
        return true;
    }    
    public boolean isPNEstrictlyConservative (){
        // Ask the Graph whether the it is bounded and the sum_of_marks is constant across all nodes.
        return coverGraph.isEstrictlyConservative();
    } 
    public boolean isPNRepetitive(List<List<Node>> scc){
        
        for (List<Node> circuit : scc) {
            if (this.hasAllTransitions(circuit)) {
                return true;
            }
        }
        //Implement tarjan algorithm to find this.
        //Whether the graph has a directed circuir with all transitions in it.
        return false;
    }  
    
    private boolean hasAllTransitions(List<Node> circuit){
        List<String> trans = new ArrayList<>();
        
        for (Node node : circuit) {
            for (Transition transition : node.getPostTransitions()) {
                if(circuit.contains(transition.getEnd())){
                    if(!trans.contains(transition.getId())){
                        trans.add(transition.getId());
                    }
                }
            }
        }
        
        return (trans.size() == this.preMatrix[0].length);
    }
    
}
