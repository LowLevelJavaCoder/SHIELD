package shield.encryption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import shield.tools.BitSplit;

public class EncryptFile {
    public Cipher cipher;
    public Path path;
    private byte[] file;
    public EncryptFile(String filePath, Cipher cipher){
        this.path = Paths.get(filePath);
        this.cipher = cipher;
    }
    public void loadFile(){
        try {
            file = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.err.println("File Too Large!");
            System.exit(69);
        }
    }

    public void encryptAsBytes(int key){
        cipher.setKey(BitSplit.intToBytes(key));
        for(int i = 0; i < file.length; i++){
            file[i] = cipher.encryptByte(file[i]);
        }
    }

    public void decryptAsBytes(int key){
        cipher.setKey(BitSplit.intToBytes(key));
        for(int i = 0; i < file.length; i++){
            file[i] = cipher.decryptByte(file[i]);
        }
    }

    public void saveFile(String outputPath){
        try {
            Files.write(Paths.get(outputPath), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encryptAsInts(int key){
        cipher.setKey(BitSplit.intToBytes(key));
        for(int i = 0; i < file.length; i += 4){
            int chunk = BitSplit.bytesToInt(new byte[]{
                file[i],
                (i + 1 < file.length) ? file[i + 1] : 0,
                (i + 2 < file.length) ? file[i + 2] : 0,
                (i + 3 < file.length) ? file[i + 3] : 0
            });
            int encryptedChunk = cipher.encryptInt(chunk);
            byte[] encryptedBytes = BitSplit.intToBytes(encryptedChunk);
            for(int j = 0; j < 4; j++){
                if(i + j < file.length){
                    file[i + j] = encryptedBytes[j];
                }
            }
        }
    }

    public void decryptAsInts(int key){
        cipher.setKey(BitSplit.intToBytes(key));
        for(int i = 0; i < file.length; i += 4){
            int chunk = BitSplit.bytesToInt(new byte[]{
                file[i],
                (i + 1 < file.length) ? file[i + 1] : 0,
                (i + 2 < file.length) ? file[i + 2] : 0,
                (i + 3 < file.length) ? file[i + 3] : 0
            });
            int decryptedChunk = cipher.decryptInt(chunk);
            byte[] decryptedBytes = BitSplit.intToBytes(decryptedChunk);
            for(int j = 0; j < 4; j++){
                if(i + j < file.length){
                    file[i + j] = decryptedBytes[j];
                }
            }
        }
    }

    public void encryptAsLongs(long key){
        cipher.setKey(BitSplit.intToBytes((int)key));
        for(int i = 0; i < file.length; i += 8){
            long chunk = BitSplit.intsToLong(new int[]{
                BitSplit.bytesToInt(new byte[]{
                    file[i],
                    (i + 1 < file.length) ? file[i + 1] : 0,
                    (i + 2 < file.length) ? file[i + 2] : 0,
                    (i + 3 < file.length) ? file[i + 3] : 0
                }),
                BitSplit.bytesToInt(new byte[]{
                    (i + 4 < file.length) ? file[i + 4] : 0,
                    (i + 5 < file.length) ? file[i + 5] : 0,
                    (i + 6 < file.length) ? file[i + 6] : 0,
                    (i + 7 < file.length) ? file[i + 7] : 0
                })
            });
            long encryptedChunk = cipher.encryptLong(chunk);
            int[] encryptedInts = BitSplit.longToInts(encryptedChunk);
            byte[] encryptedBytes1 = BitSplit.intToBytes(encryptedInts[0]);
            byte[] encryptedBytes2 = BitSplit.intToBytes(encryptedInts[1]);
            for(int j = 0; j < 4; j++){
                if(i + j < file.length){
                    file[i + j] = encryptedBytes1[j];
                }
                if(i + j + 4 < file.length){
                    file[i + j + 4] = encryptedBytes2[j];
                }
            }
        }
    }

    public void decryptAsLongs(long key){
        cipher.setKey(BitSplit.intToBytes((int)key));
        for(int i = 0; i < file.length; i += 8){
            long chunk = BitSplit.intsToLong(new int[]{
                BitSplit.bytesToInt(new byte[]{
                    file[i],
                    (i + 1 < file.length) ? file[i + 1] : 0,
                    (i + 2 < file.length) ? file[i + 2] : 0,
                    (i + 3 < file.length) ? file[i + 3] : 0
                }),
                BitSplit.bytesToInt(new byte[]{
                    (i + 4 < file.length) ? file[i + 4] : 0,
                    (i + 5 < file.length) ? file[i + 5] : 0,
                    (i + 6 < file.length) ? file[i + 6] : 0,
                    (i + 7 < file.length) ? file[i + 7] : 0
                })
            });
            long decryptedChunk = cipher.decryptLong(chunk);
            int[] decryptedInts = BitSplit.longToInts(decryptedChunk);
            byte[] decryptedBytes1 = BitSplit.intToBytes(decryptedInts[0]);
            byte[] decryptedBytes2 = BitSplit.intToBytes(decryptedInts[1]);
            for(int j = 0; j < 4; j++){
                if(i + j < file.length){
                    file[i + j] = decryptedBytes1[j];
                }
                if(i + j + 4 < file.length){
                    file[i + j + 4] = decryptedBytes2[j];
                }
            }
        }
    }

    public byte[] getFileBytes(){
        return file;
    }

    public void setFileBytes(byte[] fileBytes){
        this.file = fileBytes;
    }

    public int getFileSize(){
        return file.length;
    }
}
