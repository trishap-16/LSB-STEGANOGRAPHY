/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cryptography;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DecryptSteganography {

    public static void main(String[] args) {
        try {
            BufferedImage encryptedImage = ImageIO.read(new File("D:\\cryptography/Stgn.jpg")); // Provide the path to your encrypted image
            String decryptedMessage = decryptMessage(encryptedImage);
            System.out.println("Decrypted Message: " + decryptedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to decrypt the message from an image
    private static String decryptMessage(BufferedImage encryptedImage) {
        StringBuilder binaryMessage = new StringBuilder();

        int width = encryptedImage.getWidth();
        int height = encryptedImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = encryptedImage.getRGB(x, y);
                int blue = (pixel) & 0xff; // Get the least significant bits (LSB) from the blue channel

                // Append the LSB to the binaryMessage
                binaryMessage.append((blue & 1) == 1 ? '1' : '0');
            }
        }
        // Convert binary message to ASCII
        StringBuilder decryptedMessage = new StringBuilder();
        for (int i = 0; i < binaryMessage.length(); i += 8) {
            String binaryByte = binaryMessage.substring(i, i + 8);
            int asciiValue = Integer.parseInt(binaryByte, 2);
            decryptedMessage.append((char) asciiValue);
        }

        return decryptedMessage.toString();
    }
}
