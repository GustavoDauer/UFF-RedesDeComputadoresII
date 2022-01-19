/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author pablo
 */
public class RSAencripter {

    public static short[] encrypt(byte[] message, short n, int e) {
        short[] encriptedMessage = new short[message.length];

        for (int i = 0; i < message.length; i++) {
            encriptedMessage[i] = binExp(message[i], e, n);
        }

        return encriptedMessage;
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
}
