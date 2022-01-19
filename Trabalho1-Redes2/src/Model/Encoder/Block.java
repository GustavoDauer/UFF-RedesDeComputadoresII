/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Encoder;

import java.util.LinkedList;

/**
 *
 * @author gustavo
 */
public class Block extends LinkedList<Byte> { // Bloco de 8 bytes: Um linked list com  8 blocos de byte

    byte 
            horizontalParityByte, // Calculado a partir de 1 byte
            verticalParityByte;     // Calculado a partir de vários bytes
    
    final int FILE_SIZE_BLOCK = 10;

    /**
     * *************************************************************************************
     */
    /* O horizontalParityByte foi calculado com a função bitVerification, de maneira        */
    /* linear em horizontal.                                                                */
    /*                                                                                      */
    /* O verticalParityByte foi calculado na vertical através da função fillVerticalParity, */
    /* que usa todos os bytes do bloco ao mesmo tempo pra fazer o calculo na vertical       */
    /* acumulando os resultados e fazendo XOR bit a bit                                     */
    /**
     * *************************************************************************************
     */
    public Block() {
        this.horizontalParityByte = 0;
        this.verticalParityByte = 0;
    }

    public byte getHorizontalParityByte() {
        return horizontalParityByte;
    }

    public void setHorizontalParityByte(int position) {
        byte aux = 1;
        aux <<= position;
        
        int teste = aux;
        String teste2 = Integer.toBinaryString(teste);                
        
        this.horizontalParityByte = (byte) (horizontalParityByte | aux);                
        
        teste = this.horizontalParityByte;
        teste2 = Integer.toBinaryString(teste);        
    }

    public int getVerticalParityByte() {
        return verticalParityByte;
    }

    public void fillVerticalParity() {
        byte result = get(0);

        for (int i = 1; i < size(); i++) {
            result ^= (get(i));
        }

        this.verticalParityByte = (byte) result;
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[10];
        byteArray[0] = (byte) this.verticalParityByte; // Paridade de coluna                       
        byteArray[1] = (byte) this.horizontalParityByte; // Paridade de linha

        int listIndex = 0;

        for (int i = 2; i < FILE_SIZE_BLOCK; i++) {
            byteArray[i] = get(listIndex);
            listIndex++;
        }
        
        return byteArray;
    }
}
