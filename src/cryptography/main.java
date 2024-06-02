/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cryptography;
import static cryptography.RC6.clearPadding;
import static cryptography.RC6.decryptBlock;
import static cryptography.RC6.fillBufferZeroes;
import static cryptography.RC6.keyShedule;
import static cryptography.RC6.mergeArrays;
import static cryptography.RC6.output;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;
/**
 *
 * @author trishaprabhu
 */
class RC6 {
    public static int w = 32;
    public static int r = 20;
    public static int Pw = 0xB7E15163;
    public static int Qw = 0x9E3779b9;
    public static int[] S = new int[r * 2 + 4];
    public static byte[] output;
    public static int counter = 0;
    public static int plainTextLength;
    public static int rotateLeft(int n, int x)
    { 
        return ((n << x) | (n >>> (w - x)));
    }
    public static int rotateRight(int n, int x)
    { 
        return ((n >>> x) | (n << (w - x)));
    }
    public static byte[] convertToHex(int regA,int regB, int regC, int regD)
    {
        int[] data = new int[4];
        byte[] text = new byte[w / 2];
        data[0] = regA;
        data[1] = regB;
        data[2] = regC;
        data[3] = regD;
        for(int i = 0;i < text.length;i++)
        {
            text[i] = (byte)((data[i/4] >>> (i%4)*8) & 0xff); 
        }
        return text; 
    }
    public static void mergeArrays(byte[] array)
    { 
        for (int i = 0; i < array.length; i++)
        {
            output[counter] = array[i]; 
            counter++;
        } 
    }
    public static byte[] fillBufferZeroes(byte[] plainText)
    { 
        int length = 16 - plainText.length % 16;
        byte[] block = new byte[plainText.length + length];
        for (int i = 0; i < plainText.length; i++)
        {
            block[i] = plainText[i]; 
        }
        for(int i = plainText.length; i < plainText.length + length; i++)
        { 
            block[i] = 0;
        }
        return block; 
    }
    public static byte[] clearPadding(byte[] cipherText)
    { 
        byte[] answer = new byte[getBounds(cipherText)];
        for(int i = 0; i < cipherText.length; i++)
        {
            if(cipherText[i] == 0) break; answer[i] = cipherText[i]; 
        }
        return answer;
    }
    public static int getBounds(byte[] cipherText)
    { 
        for(int i = 0; i < cipherText.length; i++)
        {
            if(cipherText[i] == 0)
            {                
                return i;
            } 
        }
        return cipherText.length;
    }
    public static byte[] encryptBlock(byte[] plainText)
    { 
        int regA, regB, regC, regD;
        int index = 0, temp1, temp2, swap;
        regA = ((plainText[index++] & 0xff) | (plainText[index++] & 0xff) << 8|(plainText[index++] & 0xff) << 16| (plainText[index++] & 0xff)<<24);
        regB = ((plainText[index++] & 0xff) | (plainText[index++] & 0xff) << 8|(plainText[index++] & 0xff) << 16| (plainText[index++] & 0xff)<<24);
        regC = ((plainText[index++] & 0xff) | (plainText[index++] & 0xff) << 8|(plainText[index++] & 0xff) << 16| (plainText[index++] & 0xff)<<24);
        regD = ((plainText[index++] & 0xff) | (plainText[index++] & 0xff) << 8|(plainText[index++] & 0xff) << 16| (plainText[index++] & 0xff)<<24);
        regB = regB + S[0]; 
        regD = regD + S[1];
        for(int i = 1; i <= r ; i++)
        {
            temp1 = rotateLeft(regB * (regB * 2 + 1), 5);
            temp2 = rotateLeft(regD * (regD * 2 + 1), 5);
            regA = (rotateLeft(regA ^ temp1, temp2)) + S[i * 2];
            regC = (rotateLeft(regC ^ temp2, temp1)) + S[i * 2 + 1];
            swap = regA;
            regA = regB;
            regB = regC;
            regC = regD;
            regD = swap;
        }
        regA = regA + S[r * 2 + 2];
        regC = regC + S[r * 2 + 3];
        return convertToHex(regA, regB, regC, regD); 
    }
    public static byte[] decryptBlock(byte[] cipherText)
    { 
        int regA, regB, regC, regD;
        int index = 0, temp1, temp2, swap;
        regA = ((cipherText[index++] & 0xff) | (cipherText[index++] & 0xff) << 8|(cipherText[index++] & 0xff) << 16| (cipherText[index++] & 0xff)<<24);
        regB = ((cipherText[index++] & 0xff) | (cipherText[index++] & 0xff) << 8|(cipherText[index++] & 0xff) << 16| (cipherText[index++] & 0xff)<<24);
        regC = ((cipherText[index++] & 0xff) | (cipherText[index++] & 0xff) << 8|(cipherText[index++] & 0xff) << 16| (cipherText[index++] & 0xff)<<24);
        regD = ((cipherText[index++] & 0xff) | (cipherText[index++] & 0xff) << 8|(cipherText[index++] & 0xff) << 16| (cipherText[index++] & 0xff)<<24);
        regC = regC - S[r * 2 + 3]; regA = regA - S[r * 2 + 2];
        for(int i = r; i >= 1 ; i--)
        { 
            swap = regD;
            regD = regC;
            regC = regB;
            regB = regA;
            regA = swap;
            temp2 = rotateLeft(regD * (regD * 2 + 1), 5);
            temp1 = rotateLeft(regB * (regB * 2 + 1), 5);
            regC = rotateRight(regC - S[i * 2 + 1], temp1) ^ temp2;
            regA = rotateRight(regA - + S[i * 2], temp2) ^ temp1; 
        }
        regD = regD - S[1]; regB = regB - S[0];
        return convertToHex(regA, regB, regC, regD);
    }
    public static byte[] encrypt(byte[] plainText, byte[] userKey)
    {
        int blocks_number = plainText.length / 16 + 1;
        int block_counter = 0; 
        plainTextLength = plainText.length;
        output = new byte[16*blocks_number]; 
        keyShedule(userKey);
        for(int i = 0; i < blocks_number; i++)
        { 
            if(blocks_number == i + 1)
            {
                mergeArrays(encryptBlock(fillBufferZeroes(Arrays.copyOfRange(plainText, block_counter , plainText.length))));
                break; 
            }
            mergeArrays(encryptBlock(Arrays.copyOfRange(plainText, block_counter, block_counter+16)));
            block_counter += 16;
        }
        counter = 0;
        return output;
    }
    public static byte[] decrypt(byte[] cipherText, byte[] userKey)
    { 
        int blocks_number = cipherText.length / 16 + 1;
        int block_counter = 0;
        output = new byte[16*blocks_number];
        keyShedule(userKey);
        for(int i = 0; i < blocks_number; i++)
        {
            if(blocks_number == i + 1)
            {
                mergeArrays(decryptBlock(fillBufferZeroes(Arrays.copyOfRange(cipherText, block_counter ,cipherText.length))));
                break; 
            }
            mergeArrays(decryptBlock(Arrays.copyOfRange(cipherText, block_counter,block_counter+16)));
            block_counter += 16; 
        }
        counter = 0;
        return clearPadding(output); 
    }
    public static void keyShedule(byte[] key)
    { 
        int bytes = w / 8;
        int c = key.length / bytes;
        int[] L = new int[c];
        int index = 0;
        for(int i = 0; i < c; i++)
        {
            L[i] = ((key[index++]) & 0xff | (key[index++] & 0xff) << 8 | (key[index++]& 0xff) << 16 | (key[index++] & 0xff) << 24);
        }
        S[0] = Pw;
        for(int i = 1; i <= 2*r+3; i++){ S[i] = S[i-1] + Qw;
        }
        int A = 0, B = 0, i = 0,j = 0;
        int v = 3 * Math.max(c, 2*r+4);
        for(int k = 1;k <= v; k++){
            A = S[i] = rotateLeft(S[i] + A + B, 3);
            B = L[j] = rotateLeft(L[j] + A + B, A+B);
            i = (i + 1) % (2 * r + 4);
            j = (j + 1) % c; }
    } 
} 
public class main {
    public final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static void main(String[] args) throws IOException {
        encryption("D:\\cryptography/Test_AES","D:\\cryptography/RC6_KEY.txt","TEST_RC6.txt"); 
        decryption("D:\\cryptography/SteganD.txt","D:\\cryptography/RC6_KEY.txt", "RC6_D.txt");
        if(args.length == 4 ){ if(args[0].equals("-e")){
            encryption(args[1], args[2], args[3]); 
        }
        else if(args[0].equals("-d")){ decryption(args[1], args[2], args[3]);
        } 
        }
        else{
            System.out.println("Arguments not found. \n" + "Arguments list: \n"+"java Main -e textFile keyFile encryptionFile\n Encryption mode with paths to text file and key file. The last argument is encrypted text filename\n"+ "java Main -d encryptionFile keyFile outputFile\n Decryption mode with paths to encrypted text file and key file. The last argument is decryption output\n"+"----------------------------------------------------------------------------------------\n"+"Example:\n"+"Encryption: java Main -e text key enc\n"+ "Decryption: java Main -d enckey output"); 
        }
    }
    public static void encryption(String textFile, String keyFile, String outputFile){ 
        try{
            Path plainText_file = Paths.get(textFile); 
            Path key_file = Paths.get(keyFile);
            byte[] text_byte = Files.readAllBytes(plainText_file); byte[] key_byte =Files.readAllBytes(key_file);
            if(key_byte.length > 3){
                byte[] enc = RC6.encrypt(text_byte, key_byte);
                System.out.println(bytesToHex(enc));
                PrintWriter decryptFile = new PrintWriter("D:\\cryptography/"+outputFile);
                decryptFile.write(bytesToHex(enc));
                decryptFile.close();
                System.out.println("*********** Encryption is completed. Encrypted text is saved in D:\\cryptography/"+outputFile + " file ******************");
            } 
            else{
                System.out.println("Key symbols length should be >= 4\n"); }
        }
        catch (Exception e)
        {
            System.out.println("Check if plain text file or key file exists.");
            return;
        } 
    }
    public static byte[] decrypt(byte[] cipherText, byte[] userKey) {
        int blocks_number = cipherText.length / 16 + 1;
        int block_counter = 0;
        output = new byte[16 * blocks_number];
        keyShedule(userKey);

        for (int i = 0; i < blocks_number; i++) {
            if (blocks_number == i + 1) {
                mergeArrays(decryptBlock(fillBufferZeroes(Arrays.copyOfRange(cipherText, block_counter, cipherText.length))));
                break;
            }
            mergeArrays(decryptBlock(Arrays.copyOfRange(cipherText, block_counter, block_counter + 16)));
            block_counter += 16;
        }

        int counter = 0;
        return clearPadding(output);
    }
    public static void decryption(String encryptedFile, String keyFile,String outputFile){ 
        try{
            Path key_file = Paths.get(keyFile);
            BufferedReader text = new BufferedReader(new FileReader("D:\\cryptography/"+encryptedFile));
            byte[] encrypt_byte = hexStringToByteArray(text.readLine());
            byte[] key_byte = Files.readAllBytes(key_file); if(key_byte.length > 3)
            {
                byte[] dec = RC6.decrypt(encrypt_byte, key_byte);
                try (PrintWriter decryptFile = new PrintWriter("D:\\cryptography/"+outputFile)) {
                    decryptFile.write(bytesToHex(dec));
                }
                System.out.println("-----------------------DECRYPTEDTEXT--------------------------\n\n"+new String(dec)+"\n");
                System.out.println("*********** "+encryptedFile+" File decryption is done.Encrypted text is saved in Decrypted_files/"+outputFile + " file******************");
            } 
            else
            {
                System.out.println("Key symbols length should be >= 4\n");
            }
        }
        catch(Exception e)
        {
            System.out.println("Check if encrypted file or key file exists."); 
            return;
        }
    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) 
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4]; hexChars[j * 2 + 1] = hexArray[v &0x0F];
        }
        return new String(hexChars); 
    }
    public static byte[] hexStringToByteArray(String s) { int len =s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) 
    {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) +Character.digit(s.charAt(i+1), 16));
    }
    return data; 
    }
}
