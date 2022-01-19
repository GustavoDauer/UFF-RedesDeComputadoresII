/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Decoder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import Controller.DecoderController;

/**
 *
 * @author gustavo
 */
public class Decoder {
    
    String inputFileAbsolutePath;

    public Decoder(String inputFileAbsolutePath) {
        this.inputFileAbsolutePath = inputFileAbsolutePath;
    }
    
    public LinkedList<DecodedBlock> decode(DecoderController controller) throws Exception {
        byte[] data = Files.readAllBytes(Paths.get(inputFileAbsolutePath)); // Bytes do arquivo
        LinkedList<DecodedBlock> blockList = new LinkedList(); // Lista de blocos de 10 bytes
        DecodedBlock remainingBytes = null; // = new DecodedBlock(); // Lista de bytes que sobraram no final (não coube em um bloco de 8 bytes)

        int remainder = data.length % 10; // Testar se temos sobras
        if(remainder < 3 && remainder != 0){
            throw new Exception("ERROR: Last block size less than 3");
        }
        
        String teste5 = data.toString();
        int lastIndexRead = 0;
        DecodedBlock block;

        for (int i = 0; i < data.length - remainder; i+=10) {

            block = new DecodedBlock(data[i],data[i+1]);                    
            int j;
            
            for(j=i+2; j<i+10; j++) {                
                block.add(data[j]);  
            }                        

            blockList.add(block);
            lastIndexRead = j; // Guardar ultimo index lido para os remanescentes
        }

        if (remainder > 0) { // Armazenar os bytes restantes do arquivo            
            block = new DecodedBlock(data[data.length-remainder],data[data.length-remainder+1]);

            for (int i = lastIndexRead+3; i < data.length; i++) {               
                block.add(data[i]);
            }

            remainingBytes = block;
            
            // Preencher os bytes restantes com zeros
            while (remainingBytes.size() < 8) {
                byte zeroByte = 0;
                remainingBytes.add(zeroByte);
            }
            
            // Adicionar o bloco preenchido na lista de blocos
            blockList.add(remainingBytes);
        }

        // Calcular bit de paridade horizontal                   
        for (int i = 0; i < blockList.size(); i++) {
            for (int j = 0; j < blockList.get(i).size(); j++) {

                if (bitVerification(blockList.get(i).get(j)) == true) {
                    blockList.get(i).setHorizontalParityByte(j);
                    int teste = blockList.get(i).getHorizontalParityByte();
                    String teste2 = Integer.toBinaryString(teste);                    
                }
            }

            blockList.get(i).fillVerticalParity(); // Calcular bit de paridade vertical
            
            int [] errorPosition = blockList.get(i).compareParity();
            
            if(errorPosition[0] != -1 && errorPosition[1] != -1) {
                controller.printMessage("error found in block: "+i+"; line:" + errorPosition[1]+ "column: "+ errorPosition[0]);
                blockList.get(i).fixBlock(errorPosition);
                controller.printMessage("block fixed: "+i+"; line:" + errorPosition[1]+ "column: "+ errorPosition[0]);                
            }
        }

        return blockList;
    }

    public String getInputFileAbsolutePath() {
        return inputFileAbsolutePath;
    }

    private static boolean bitVerification(byte data) {

        byte elem = 1;
        boolean isOne = false;

        for (int i = 0; i < 8; i++) {
            if ((data & elem) > 0) {
                isOne = !isOne;
            }

            elem <<= 1;
        }

        return isOne;
    }
    
}
