/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Decoder.Decoder;
import Model.Decoder.DecodedBlock;
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
public class DecoderController {

    Decoder decoder;
    JTextArea mainLogView;
    String inputFileAbsolutePath, outputFileAbsolutePath;

    public DecoderController(JTextArea mainLogView, String inputFileAbsolutePath, String outputFileAbsolutePath) {
        this.inputFileAbsolutePath = inputFileAbsolutePath;
        this.outputFileAbsolutePath = outputFileAbsolutePath;
        this.mainLogView = mainLogView;

        decoder = new Decoder(inputFileAbsolutePath);
    }

    public void decode() {
        
        try {
            LinkedList<DecodedBlock> result = decoder.decode(this);
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
    
    public void printMessage(String message){
        mainLogView.setText(message);
    }

    
}
