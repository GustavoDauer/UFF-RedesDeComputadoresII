/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JTextArea;

/**
 *
 * @author gustavo
 */
public class RSAdecrypterController {

    String output;
    JTextArea results;

    public RSAdecrypterController(String out, JTextArea r) {
        output = out;
        results = r;
    }

    public void writeFile(byte[] decrypted) throws IOException {
        Path path = Paths.get(output);
        Files.write(path, decrypted);
        
        showFile(output);
    }
    
    public void showFile(String fileName) throws IOException {
        File file = new File(fileName);

        if (file.exists() && !file.isDirectory()) {

            results.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            results.append(fileName + "\n");
            results.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            
            FileInputStream fis = new FileInputStream(file);
            byte[] bytesToRead = new byte[1];
            
            while(fis.read(bytesToRead) > 0) {
                String text = new String(bytesToRead);
                results.append(text);
            }
            
            results.append("\n\n");
        }
        else {
            //throws
        }
    }
}
