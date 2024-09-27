import java.util.Scanner;

public class MainProgram {

    private static final String[] SUBSTITUTION = {
            "1100",
            "0110",
            "1001",
            "0100",
            "1111",
            "0011",
            "1010",
            "0001",
            "1101",
            "1110",
            "0111",
            "0101",
            "0010",
            "1000",
            "0000",
            "1011"
    };

    public static String substitute(String fourBits) {
        int index = Integer.parseInt(fourBits, 2);
        return SUBSTITUTION[index];
    }

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

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xFF));
        }
        return sb.toString();
    }

    public static byte[] ecbEncrypt(byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        for (int i = 0; i < plaintext.length; i++) {
            byte currentByte = plaintext[i];
            String highNibble = String.format("%4s", Integer.toBinaryString((currentByte >> 4) & 0x0F)).replace(' ',
                    '0');
            String lowNibble = String.format("%4s", Integer.toBinaryString(currentByte & 0x0F)).replace(' ', '0');
            String substitutedHigh = substitute(highNibble);
            String substitutedLow = substitute(lowNibble);
            String combined = substitutedHigh + substitutedLow;
            ciphertext[i] = (byte) Integer.parseInt(combined, 2);
        }
        return ciphertext;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter plaintext in hex: ");
        String plaintextHex = scanner.nextLine().trim();
        byte[] plaintext = hexStringToByteArray(plaintextHex);

        byte[] ecbCipher = ecbEncrypt(plaintext);

        System.out.println("Plaintext: " + plaintextHex.toLowerCase());
        System.out.println("ECB Ciphertext: " + byteArrayToHexString(ecbCipher).toLowerCase());

        scanner.close();
    }
}
