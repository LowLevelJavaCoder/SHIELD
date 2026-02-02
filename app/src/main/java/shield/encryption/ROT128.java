package shield.encryption;

import shield.tools.BitSplit;

public class ROT128 implements Cipher{
    public ROT128() {}

    public void setKey(byte[] key){}

    public byte[] getKey(){return new byte[0];}

    @Override
    public byte encryptByte(byte input){
        return (byte) (input + 128);
    }
    @Override
    public byte decryptByte(byte input){
        return (byte) ((input & 0xFF) - 128);
    }
    @Override
    public byte[] encryptBytes(byte[] input){
        for (int i = 0; i < input.length; i++){
            input[i] = encryptByte(input[i]);
        }
        return input;
    }
    @Override
    public byte[] decryptBytes(byte[] input){
        for (int i = 0; i < input.length; i++){
            input[i] = decryptByte(input[i]);
        }
        return input;
    }
    @Override
    public int encryptInt(int input){
        byte[] bytes = BitSplit.intToBytes(input);
        byte[] outputBytes = encryptBytes(bytes);
        int output = BitSplit.bytesToInt(outputBytes);
        return output;
    }
    @Override
    public int decryptInt(int input){
        byte[] bytes = BitSplit.intToBytes(input);
        byte[] outputBytes = decryptBytes(bytes);
        int output = BitSplit.bytesToInt(outputBytes);
        return output;
    }
    @Override
    public long encryptLong(long input){
        int[] ints = BitSplit.longToInts(input);
        int[] outputInts = new int[2];
        outputInts[0] = encryptInt(ints[0]);
        outputInts[1] = encryptInt(ints[1]);
        long output = BitSplit.intsToLong(outputInts);
        return output;
    }
    @Override
    public long decryptLong(long input){
        int[] ints = BitSplit.longToInts(input);
        int[] outputInts = new int[2];
        outputInts[0] = decryptInt(ints[0]);
        outputInts[1] = decryptInt(ints[1]);
        long output = BitSplit.intsToLong(outputInts);
        return output;
    }
}
