/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cryptography;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class TripleDESTest 
{
    public static void main(String... args) 
    { 
        try
        {
            String KEY_STRING = "asdasdasd"; 
            byte[] key = getEnKey(KEY_STRING);
            String pFilePath = "D:\\cryptography\\test.txt";
            String pFilePathEncryp = "D:\\cryptography/Test_3DES";
            String pFilePathDecryp = "D:\\Decrypt/Test_Decrypted.txt";
            byte[]archivoEncrypt = encryptFile(pFilePath, key);
            try (FileOutputStream fos = new FileOutputStream(pFilePathEncryp))
            {
                fos.write(archivoEncrypt);
            }
            byte[] archivoDecrypt = decryptFile(pFilePathEncryp, key);
            try (FileOutputStream fos = new FileOutputStream(pFilePathDecryp)) {
                fos.write(archivoDecrypt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    public static byte[] encryptFile(String pFilePath, byte[] pKey) throws GeneralSecurityException, IOException 
    {
        File file = new File(pFilePath); 
        long length = file.length();
        InputStream is = new FileInputStream(file);
        if (length > Integer.MAX_VALUE) 
        {
            // File is too large 
        }
        byte[] bytes = new byte[(int) length];
        // Read in the bytes 
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) 
        {
            offset += numRead;
        }
       
        is.close();
       
        if (offset < bytes.length) {
        throw new IOException("Could not completely read file " + file.getName());
        }
        SecretKeyFactory lDESedeKeyFactory = SecretKeyFactory.getInstance("DESede"); 
        SecretKey kA = lDESedeKeyFactory.generateSecret(new DESedeKeySpec(pKey));
        IvParameterSpec lIVSpec = new IvParameterSpec(new byte[8]);
        Cipher desedeCBCCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        desedeCBCCipher.init(Cipher.ENCRYPT_MODE, kA, lIVSpec);
        byte[] encrypted = desedeCBCCipher.doFinal(bytes);
        return encrypted; 
    }
    public static byte[] decryptFile(String pFilePathEncryp, byte[] pKey) throws Exception {
        File file = new File(pFilePathEncryp);
        long length = file.length();
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        is.close();

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        SecretKeyFactory lDESedeKeyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey kA = lDESedeKeyFactory.generateSecret(new DESedeKeySpec(pKey));
        IvParameterSpec lIVSpec = new IvParameterSpec(new byte[8]);
        Cipher desedeCBCCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        desedeCBCCipher.init(Cipher.DECRYPT_MODE, kA, lIVSpec);
        byte[] decrypted = desedeCBCCipher.doFinal(bytes);
        return decrypted;
    }
    private static byte[] getEnKey(String spKey) 
    { 
        byte[] desKey = null;
        try 
        {
            byte[] desKey1 = md5(spKey); desKey = new byte[24];
            int i = 0;
            while (i < desKey1.length && i < 24) 
            {
                desKey[i] = desKey1[i];
                i++; 
            }
            if (i < 24) 
            { 
                desKey[i] = 0; 
                i++;
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
        return desKey; 
    }
    private static byte[] md5(String strSrc) { byte[] returnByte = null;
    try 
    {
        MessageDigest md5 = MessageDigest.getInstance("MD5"); 
        returnByte = md5.digest(strSrc.getBytes("GBK"));
    } catch (Exception e) { 
        e.printStackTrace();
    }
    return returnByte;
    }
}