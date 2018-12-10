/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphTools;

/**
 *
 * @author Tania Rodriguez
 */
public class Transition {
    
    private String id;
    private Node postPlace;
    
    public Transition(String id){
        this.id = id;
    }
    
    public String getId(){return this.id;}
    public Node getPostPlace(){return this.postPlace;}
    
    public void setId(String id){this.id = id;}
    public void setEnd(Node end){this.postPlace = end;}
    public void setTransitionTo(Node node){postPlace = node;}
    
    public Node getEnd(){ return postPlace; }    
}
