package shield;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ArgGroup;

import shield.encryption.Cipher;
import shield.encryption.ROT128;
import shield.encryption.EncryptFile;

import java.util.concurrent.Callable;

@Command(name = "shield", mixinStandardHelpOptions = true, version = "0.1.0",
        description = "Short-key Hashing & Insecure Encryption for Developers (SHIELD)")
public class App implements Callable<Integer> {

    // Mutually exclusive input: file OR string
    static class InputGroup {
        @Option(names = "--file", description = "Path to file to encrypt/decrypt")
        String filePath;

        @Option(names = "--string", description = "String to encrypt/decrypt")
        String inputString;
    }

    @ArgGroup(exclusive = true, multiplicity = "1")
    InputGroup inputGroup;

    // Key is optional with default 67
    @Option(names = {"-k", "--key"}, description = "Encryption key (integer, default=67)")
    Integer key = 67;

    @Option(names = {"-m", "--mode"}, description = "Mode: bytes | ints | longs", required = true)
    String mode;

    @Option(names = {"-d", "--decrypt"}, description = "Decrypt instead of encrypt")
    boolean decrypt;

    @Option(names = {"-o", "--output"}, description = "Output file path (required for file input)")
    String outputPath;

    // Cipher selection: optional, default ROT128
    @Option(names = {"-c", "--cipher"}, description = "Cipher to use (default=ROT128)")
    String cipherName = "ROT128";

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        Cipher cipher = getCipherByName(cipherName);

        if (cipher == null) {
            System.err.println("Unknown cipher: " + cipherName);
            return 1;
        }

        if (inputGroup.filePath != null) {
            return handleFileInput(cipher);
        } else {
            return handleStringInput(cipher);
        }
    }

    private Cipher getCipherByName(String name) {
        // Add more ciphers here as needed
        switch (name.toUpperCase()) {
            case "ROT128":
                return new ROT128();
            // case "OTHER": return new OtherCipher();
            default:
                return null;
        }
    }

    private Integer handleFileInput(Cipher cipher) {
        EncryptFile ef = new EncryptFile(inputGroup.filePath, cipher);
        ef.loadFile();

        switch (mode.toLowerCase()) {
            case "bytes":
                if (decrypt) ef.decryptAsBytes(key);
                else ef.encryptAsBytes(key);
                break;
            case "ints":
                if (decrypt) ef.decryptAsInts(key);
                else ef.encryptAsInts(key);
                break;
            case "longs":
                if (decrypt) ef.decryptAsLongs(key);
                else ef.encryptAsLongs(key);
                break;
            default:
                System.err.println("Unknown mode: " + mode);
                return 1;
        }

        if (outputPath == null) {
            System.err.println("Output path required for file input!");
            return 1;
        }

        ef.saveFile(outputPath);
        System.out.println("File processed successfully: " + outputPath);
        return 0;
    }

    private Integer handleStringInput(Cipher cipher) {
        byte[] inputBytes = inputGroup.inputString.getBytes();
        EncryptFile ef = new EncryptFile("virtual", cipher);
        ef.setFileBytes(inputBytes);

        switch (mode.toLowerCase()) {
            case "bytes":
                if (decrypt) ef.decryptAsBytes(key);
                else ef.encryptAsBytes(key);
                break;
            case "ints":
                if (decrypt) ef.decryptAsInts(key);
                else ef.encryptAsInts(key);
                break;
            case "longs":
                if (decrypt) ef.decryptAsLongs(key);
                else ef.encryptAsLongs(key);
                break;
            default:
                System.err.println("Unknown mode: " + mode);
                return 1;
        }

        System.out.println("Result: " + new String(ef.getFileBytes()));
        return 0;
    }
}
