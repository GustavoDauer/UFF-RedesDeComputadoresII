/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import javax.swing.JTextArea;

/**
 *
 * @author gustavo
 */
public class RSAencripterController {

    String output;
    JTextArea results;

    public RSAencripterController(String out, JTextArea r) {
        output = out;
        results = r;
    }

    public void writeFile(short[] encrypted) throws FileNotFoundException, IOException {
        ByteBuffer myByteBuffer = ByteBuffer.allocate(encrypted.length * 2);
        //myByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        ShortBuffer myShortBuffer = myByteBuffer.asShortBuffer();
        myShortBuffer.put(encrypted);

        FileChannel out = new FileOutputStream(output).getChannel();
        out.write(myByteBuffer);
        out.close();

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
