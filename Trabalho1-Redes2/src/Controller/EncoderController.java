/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Encoder.Block;
import Model.Encoder.Encoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import javax.swing.JTextArea;

/**
 *
 * @author gustavo
 */
public class EncoderController {

    Encoder encoder;
    JTextArea mainLogView;
    String inputFileAbsolutePath, outputFileAbsolutePath;

    public EncoderController(JTextArea mainLogView, String inputFileAbsolutePath, String outputFileAbsolutePath) {
        this.inputFileAbsolutePath = inputFileAbsolutePath;
        this.outputFileAbsolutePath = outputFileAbsolutePath;
        this.mainLogView = mainLogView;

        encoder = new Encoder(inputFileAbsolutePath);
    }

    public void encode() {
        try {
            LinkedList<Block> result = encoder.encode();
            String exibition = "";
            Path path = Paths.get(this.outputFileAbsolutePath);       
            if(Files.exists(path))
                Files.delete(path);
            
            for (int i = 0; i < result.size(); i++) {                                
                Files.write(path, result.get(i).toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                byte[] fileBlock = result.get(i).toByteArray();

                for (int j = 0; j < fileBlock.length; j++) {
                    exibition += "\n" + Integer.toBinaryString(fileBlock[j]) + "\n";                    
                }
            }

            mainLogView.setText(exibition);
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            mainLogView.setText(e.getLocalizedMessage());
        }
    }

}
