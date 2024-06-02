/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cryptography;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES {
    public static void processFilePath(String filePath) {
        // Do something with the filePath
        System.out.println("Received file path: " + filePath);
        String path = filePath;
        // You can perform further processing with the file path here
    }
    
    public static void main(String[] args) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,IllegalBlockSizeException, InvalidKeyException 
    {
        System.out.println("AES ECB Stream Encryption");
        System.out.println("* * * WARNING Do NOT use AES ECB mode in production as it is UNSECURE ! * * *");
        String plaintextFilename = "D://cryptography/Test_3DES";
        String ciphertextFilename = "D:\\cryptography/Test_AES";
        String decryptedtextFilename = "D:\\Decrypt/Test_D_AES";
        byte[] key = "12345678901234561234567890123456".getBytes("UTF-8");
        // 32 byte = 256 bit key length
        encryptWitEcb(plaintextFilename, ciphertextFilename, key);
        decryptWithEcb(ciphertextFilename, decryptedtextFilename, key);
        System.out.println("file used for encryption: " + plaintextFilename);
        System.out.println("created encrypted file : " + ciphertextFilename);
        System.out.println("created decrypted file : " + decryptedtextFilename);
        System.out.println("AES ECB Stream Encryption ended");
    }
    public static void encryptWitEcb(String filenamePlain, String filenameEnc, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        try (FileInputStream fis = new FileInputStream(filenamePlain);
                BufferedInputStream in = new BufferedInputStream(fis);
                FileOutputStream out = new FileOutputStream(filenameEnc);
                BufferedOutputStream bos = new BufferedOutputStream(out))
        {
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) 
            {
                byte[] obuf = cipher.update(ibuf, 0, len); if (obuf != null)bos.write(obuf); 
            }
            byte[] obuf = cipher.doFinal(); 
            if (obuf != null)
                bos.write(obuf); 
        }
    }
    public static void decryptWithEcb(String filenameEnc, String filenameDec, byte[] key) throws IOException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,IllegalBlockSizeException, BadPaddingException 
    {
        try (FileInputStream in = new FileInputStream(filenameEnc);
                FileOutputStream out = new FileOutputStream(filenameDec)) 
        {
            byte[] ibuf = new byte[1024];
            int len;
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            while ((len = in.read(ibuf)) != -1) 
            {
                byte[] obuf = cipher.update(ibuf, 0, len);
                if (obuf != null)
                    out.write(obuf); 
            }
            byte[] obuf = cipher.doFinal();
            if (obuf != null)
                out.write(obuf); 
        }
    }
}
