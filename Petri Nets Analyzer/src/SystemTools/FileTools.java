/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SystemTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

/**
 *
 * @author gamaa
 */
public class FileTools {
    public static String readFile(File file) {
        String content = "";
        try (InputStream in = Files.newInputStream(file.toPath());
                BufferedReader reader
                = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                content = content + line + "\n";
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        return content;
    }

    /**
     * write a file
     *
     * @param name
     * @param cont
     */
    public static void write(String name, String cont, String ext) {
        FileWriter fichero = null;
        BufferedWriter pw = null;
        try {
            fichero = new FileWriter(name + "."+ext);
            pw = new BufferedWriter(fichero);
            pw.write(cont);
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    
     /**
     * generate the image file to graphviz
     * @param fileName
     * @param format 
     */
    public static void generateImg(String fileName, String format) {
        try {
            String cmd = "bin\\dot.exe" + " -T" + format + " " +  fileName + ".txt "
                    + "-o " + fileName + "." + format;
            Runtime.getRuntime().exec(cmd);
        } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
