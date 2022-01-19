/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Encoder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 *
 * @author gustavo
 */
public class Encoder {

    String inputFileAbsolutePath;

    public Encoder(String inputFileAbsolutePath) {
        this.inputFileAbsolutePath = inputFileAbsolutePath;
    }

    public LinkedList<Block> encode() throws Exception {

        ////////////////////////////////////////////
        // Leitura do arquivo e divisão em blocos //
        ////////////////////////////////////////////
        byte[] data = Files.readAllBytes(Paths.get(inputFileAbsolutePath)); // Bytes do arquivo
        LinkedList<Block> blockList = new LinkedList(); // Lista de blocos de 8 bytes
        Block remainingBytes = new Block(); // Lista de bytes que sobraram no final (não coube em um bloco de 8 bytes)

        int remainder = data.length % 8; // Testar se temos sobras
        int lastIndexRead = 0;
        Block block;

        for (int i = 0; i < data.length - remainder; i+=8) {

            block = new Block();                    
            int j;
            
            for(j=i; j<i+8; j++) {
                block.add(data[j]);  
            }

            blockList.add(block);
            lastIndexRead = j; // Guardar ultimo index lido para os remanescentes
        }

        if (remainder > 0) { // Armazenar os bytes restantes do arquivo            
            block = new Block();

            for (int i = lastIndexRead+1; i < data.length; i++) {               
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
