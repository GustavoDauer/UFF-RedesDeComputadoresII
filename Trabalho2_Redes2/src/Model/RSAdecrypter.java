/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;

/**
 *
 * @author pablo
 */
public class RSAdecrypter {

    public static byte[] decrypt(short[] message, short n, int e) throws Exception {
        byte[] decriptedMessage = new byte[message.length];

        for (int i = 0; i < message.length; i++) {
            short sh = binExp(message[i], e, n);
            if (sh < 256) {
                byte bt = (byte) sh;
                decriptedMessage[i] = bt;
            } else {
                throw new Exception("Erro na decriptação: Byte maior que 255");                
            }
        }

        return decriptedMessage;
    }

    public static short binExp(short b, int e, short n) {

        int res = b;
        int y = 1;

        if (e == 0) {
            return 1;
        }

        while (e > 1) {

            if ((e & 1) > 0) {
                y = (y * res) % (int) n;
                e = e - 1;
            }

            res = (res * res) % (int) n;
            e = e / 2;
        }

        return (short) (((res * y) % n));
    }

    public static short twoBytesToShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }
}
