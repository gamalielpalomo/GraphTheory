/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Global;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import SystemTools.FileTools;
import GraphTools.Graph;

/**
 *
 * @author gamaa
 */
public class Globals {
    
    public static int [][]preMatrix;
    public static int [][]postMatrix;
    public static int []marking;
    public static int DELAY = 300;
    
    
    public static void printMatrix(int[][] matrix){
        for (int[] vector : matrix) {
            for (int column = 0; column < matrix[0].length; column++) {
                System.out.print(vector[column] + " ");
            }
            System.out.println();
        }
    }
    
    public static void printVector(int[] vector){
        for (int element : vector)
            System.out.print(element + " ");
        System.out.println();
    }
    
    public static void makeGraph(int[][] mpre, int[][] mpost) {
        String nombreArchivo = "PetriNetwork";
        String content = "digraph PetriNetwork          {\n"
                + "rankdir=LR;"
                + //girar a la derecha
                //"# page = \"8.2677165,11.692913\" ;\n" +
                "ratio = \"auto\" ;\n"
                + "mincross = 6.0 ;\n";

        int[][] pre = mpre;
        int[][] post = mpost;

        for (int i = 0; i < pre.length; i++) {
            //Se instancias las P
            content = content + "\"P" + (i) + "\" [shape=circle  , regular=1,style=filled,fillcolor=white] ;\n";
        }
        for (int i = 0; i < pre[0].length; i++) {
            //Se instancias las T
            content = content + "\"t" + (i) + "\" [shape=box,label=\"t" + (i) + "\",height=.1,width=.1] ;\n";
        }
        String label = "";
        for (int i = 0; i < pre.length; i++) {
            //Se Instancian las P
            for (int j = 0; j < pre[0].length; j++) {
                //Se generan las transiciones
                if (pre[i][j] > 0) {//de P's  a T's
                    label = (pre[i][j] == 1)? "": String.valueOf(pre[i][j]);
                    content = content + "\"P" + (i) + "\" -> \"t" + (j) + "\" [dir=normal,weight=1, label=\""+label+"\"] ;\n";
                }
                if (post[i][j] > 0) {//de T's  a P's
                    label = (post[i][j] == 1)? "": String.valueOf(post[i][j]);
                    content = content + "\"t" + (j) + "\" -> \"P" + (i) + "\" [dir=normal,weight=1, label=\""+label+"\"] ;\n";
                }
            }
        }
        content = content + "}";
        //System.out.print(content);
        FileTools.write(nombreArchivo, content, "txt");
        FileTools.generateImg(nombreArchivo, "png");
        
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    public static void makeCoverGraph(Graph gcover, String fileName) {
        System.out.println("Nodes in "+fileName+": "+gcover.getNodes().size());
        
        String nombreArchivo = fileName;
        String content = "digraph CoverGraph          {\n"
                //+ "rankdir=LR;"
                + //girar a la derecha
                //"# page = \"8.2677165,11.692913\" ;\n" +
                "ratio = \"auto\" ;\n"
                + "mincross = 6.0 ;\n";

        for(int i=0;i<gcover.getNodes().size();i++){
            content = content + "\"" + gcover.getNodes().get(i).getMarkString() + "\" [shape=box,label=\"" + gcover.getNodes().get(i).getMarkString() + "\",height=.1,width=.1] ;\n";
        }
        
        for(int i=0;i<gcover.getNodes().size();i++){
            //System.out.println(""+gcover.getNodes().get(i).getParent().getMarkString()+"->"+gcover.getNodes().get(i).getMarkString()+"sda");           
            for( int j=0; j<gcover.getNodes().get(i).getPostTransitions().size(); j++ ){
                try{
                //System.out.println(""+gcover.getNodes().get(i).getMarkString()+"->"+gcover.getNodes().get(i).getTransitions().get(j).getEnd().getMarkString()+" : "+gcover.getNodes().get(i).getTransitions().get(j).getId());
                content = content + "\"" + gcover.getNodes().get(i).getMarkString() + "\" -> \"" + gcover.getNodes().get(i).getPostTransitions().get(j).getEnd().getMarkString() + "\" [dir=normal,weight=1,label = \""+gcover.getNodes().get(i).getPostTransitions().get(j).getId()+"\"] ;\n";
                }catch(NullPointerException npe){
                    System.out.println(npe.getMessage());
                }
            }
        }
              
        content = content + "}";
        //System.out.print(content);
        FileTools.write(nombreArchivo, content, "txt");
        FileTools.generateImg(nombreArchivo, "png");
        
        try {
            Thread.sleep( DELAY );
        } catch (InterruptedException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //new GraphFrame(nombreArchivo);
        BufferedImage img = null;
        JLabel picLabel = null;
        try {
            img = ImageIO.read(new File("CoverGraph.png"));
            picLabel = new JLabel(new ImageIcon(img));
        } catch (IOException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
