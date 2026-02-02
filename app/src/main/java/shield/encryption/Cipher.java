package shield.encryption;

public interface Cipher {
    byte encryptByte(byte input);
    int encryptInt(int input);
    long encryptLong(long input);
    byte[] encryptBytes(byte[] input);
    byte decryptByte(byte input);
    int decryptInt(int input);
    long decryptLong(long input);
    byte[] decryptBytes(byte[] input);
    void setKey(byte[] key);
    byte[] getKey();
}
