package shield.tools;

public class BitSplit {
    public static byte[] intToBytes(int input){
        byte[] bytes = new byte[4];

        bytes[0] = (byte) ((input >> 24) & 0xFF);
        bytes[1] = (byte) ((input >> 16) & 0xFF);
        bytes[2] = (byte) ((input >> 8) & 0xFF);
        bytes[3] = (byte) (input & 0xFF);

        return bytes;
    }

    public static int bytesToInt(byte[] input){
        int output = 
            ((input[0] & 0xFF) << 24) |
            ((input[1] & 0xFF) << 16) |
            ((input[2] & 0xFF) << 8) |
            (input[3] & 0xFF);
        return output;
    }

    public static long intsToLong(int[] input){
        long output = 
            ((input[0] & 0xFFFFFFFFL) << 32) |
            ((input[1] & 0xFFFFFFFFL));
        return output;
    }

    public static int[] longToInts(long input) {
        int[] ints = new int[2];
        ints[0] = (int) ((input >> 32) & 0xFFFFFFFFL);
        ints[1] = (int) (input & 0xFFFFFFFFL);
        return ints;
    }
}
