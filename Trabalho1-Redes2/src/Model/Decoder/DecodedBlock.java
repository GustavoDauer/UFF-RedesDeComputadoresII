/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Decoder;

import java.util.LinkedList;

/**
 *
 * @author Pablo
 */
public class DecodedBlock extends LinkedList<Byte>{
    
    byte horizontalParityByte; // Calculado a partir de 1 byte
    byte verticalParityByte;     // Calculado a partir de vários bytes
    
    byte oldHorizontalParityByte;
    byte oldVerticalParityByte;
            
            
    
    
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
    public DecodedBlock(byte oldVertical, byte oldHorizontal) {
        this.horizontalParityByte = 0;
        this.verticalParityByte = 0;
        
        oldHorizontalParityByte = oldHorizontal;
        oldVerticalParityByte = oldVertical;
    }

    public int getHorizontalParityByte() {
        return horizontalParityByte;
    }

    public void setHorizontalParityByte(int position) {
        int aux = 1;
        aux <<= position;
        
        int teste = aux;
        String teste2 = Integer.toBinaryString(teste);                
        
        this.horizontalParityByte = (byte)(horizontalParityByte | aux);                
        
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
        byteArray[0] = this.verticalParityByte; // Paridade de coluna                       
        byteArray[1] = this.horizontalParityByte; // Paridade de linha

        int listIndex = 0;

        for (int i = 2; i < FILE_SIZE_BLOCK; i++) {
            byteArray[i] = get(listIndex);
            listIndex++;
        }
        
        return byteArray;
    }
    
    public int[] compareParity() throws DecoderException{
        
        int[] position = new int[3];
        
        LinkedList<Integer> XexceptionPostions = new LinkedList();
        LinkedList<Integer> YexceptionPostions = new LinkedList();
        
        int X = -1;
        int Y = -1;
        
        //Compara a paridade calculada depois da leitura com a paridade lida do arquivo.
        byte horizontalXOR = (byte)(oldHorizontalParityByte ^ horizontalParityByte);
        byte verticalXOR = (byte)(oldVerticalParityByte ^ verticalParityByte);
        
        //Retorna posições invalidas indicando que não há erros no bloco;
        if(horizontalXOR == 0 && verticalXOR == 0){
            position[0] = X;
            position[1] = Y;            
            return position;
        }
        else{
            //Aqui serão contados o número de erros encontrados no bloco, além de atualizar a posição dos mesmos
            //se mais de um erro for encontrado uma exceção será jogada contendo as posições de erros
            int xcounter = 0;
            int ycounter = 0;
            
            byte aux = 1;
            for(int i = 0; i < 8; i++){
                
                if( (aux^horizontalXOR) > 0){
                    position[0] = i;
                    XexceptionPostions.push(i);
                    xcounter++;
                }
                if( (aux^verticalXOR) > 0){
                    position[1] = i;
                    YexceptionPostions.push(i);
                    ycounter++;
                }          
                
                aux <<= 1;
                
            }
            
            if(xcounter == 1 && ycounter == 1){
                return position;
            }
            else{   
                position[0] = X;
                position[1] = Y;
                String exception = new String();
                
                for(int i = 0; i < XexceptionPostions.size(); i++){
                    exception += " column = " + XexceptionPostions.get(i) + "," ;
                }
                for(int i = 0; i < YexceptionPostions.size(); i++){
                    exception += " line = " + YexceptionPostions.get(i) + "," ;
                }
                
                
                throw new DecoderException("More than one error found. Positions: {" + exception +"}.");
            }
                       
            
        }          

    }
    
    public void fixBlock(int errorPos[]) {
        int x = errorPos[0];
        int y = errorPos[1];
        
        byte line = get(y);        
        byte aux = 1;
        
        aux <<= x;
        line ^= aux;
        
        remove(y);
        add(y, line);                
    }
    
}
