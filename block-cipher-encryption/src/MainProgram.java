import java.util.Scanner;

/**
 * This class provides methods for converting between hexadecimal strings and
 * byte arrays.
 * It also includes the main method, which is the entry point of the program.
 *
 * @author Matthew Quijano
 */
public class MainProgram {

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param s the hexadecimal string to be converted
     * @return the byte array representation of the hexadecimal string
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            s = "0" + s;
            len = s.length();
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Converts a byte array to a hexadecimal string representation.
     *
     * @param bytes the byte array to be converted
     * @return the hexadecimal string representation of the byte array
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    /**
     * The main method is the entry point of the program.
     * It prompts the user to enter plaintext in hexadecimal format and an IV
     * (Initialization Vector) in hexadecimal format.
     * It then performs encryption using three different modes: ECB (Electronic
     * Codebook), CTR (Counter), and CBC (Cipher Block Chaining).
     * The ciphertexts generated using each mode are displayed on the console.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter plaintext in hex: ");
        String plaintextHex = scanner.nextLine().trim();
        byte[] plaintext = hexStringToByteArray(plaintextHex);

        System.out.print("Enter IV in hex (single hex digit): ");
        String ivHex = scanner.nextLine().trim();
        if (ivHex.length() != 1) {
            System.out.println("IV should be a single hex digit.");
            scanner.close();
            return;
        }
        String ivBinary = String.format("%4s", Integer.toBinaryString(Integer.parseInt(ivHex, 16))).replace(' ', '0');

        byte[] ecbCipher = ECBModeEncryption.ecbEncrypt(plaintext);
        byte[] ctrCipher = CTRModeEncryption.ctrEncrypt(ivHex, plaintext);
        byte[] cbcCipher = CBCModeEncryption.cbcEncrypt(ivBinary, plaintext);

        System.out.println("Plaintext: " + plaintextHex.toLowerCase());
        System.out.println("ECB Ciphertext, in hex: " + byteArrayToHexString(ecbCipher).toLowerCase());
        System.out.println("CTR Ciphertext, in hex: " + byteArrayToHexString(ctrCipher).toLowerCase());
        System.out.println("CBC Ciphertext, in hex: " + byteArrayToHexString(cbcCipher).toLowerCase());

        scanner.close();
    }
}
