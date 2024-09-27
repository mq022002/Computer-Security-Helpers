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

    public static byte[] ctrEncrypt(String iv, byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        int counter = Integer.parseInt(iv, 16) & 0x0F;
        for (int i = 0; i < plaintext.length; i++) {
            byte currentByte = plaintext[i];
            String highNibble = String.format("%4s", Integer.toBinaryString((currentByte >> 4) & 0x0F)).replace(' ',
                    '0');
            String lowNibble = String.format("%4s", Integer.toBinaryString(currentByte & 0x0F)).replace(' ', '0');

            String counterBinary = String.format("%4s", Integer.toBinaryString(counter)).replace(' ', '0');
            String encryptedCounterHigh = substitute(counterBinary);
            String encryptedHigh = xorBits(encryptedCounterHigh, highNibble);
            counter = (counter + 1) & 0x0F;

            counterBinary = String.format("%4s", Integer.toBinaryString(counter)).replace(' ', '0');
            String encryptedCounterLow = substitute(counterBinary);
            String encryptedLow = xorBits(encryptedCounterLow, lowNibble);
            counter = (counter + 1) & 0x0F;

            String combined = encryptedHigh + encryptedLow;
            ciphertext[i] = (byte) Integer.parseInt(combined, 2);
        }
        return ciphertext;
    }

    public static byte[] cbcEncrypt(String iv, byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        String previousCipher = iv;
        for (int i = 0; i < plaintext.length; i++) {
            byte currentByte = plaintext[i];
            String highNibble = String.format("%4s", Integer.toBinaryString((currentByte >> 4) & 0x0F)).replace(' ',
                    '0');
            String lowNibble = String.format("%4s", Integer.toBinaryString(currentByte & 0x0F)).replace(' ', '0');
            String xoredHigh = xorBits(previousCipher, highNibble);
            String substitutedHigh = substitute(xoredHigh);
            previousCipher = substitutedHigh;
            String xoredLow = xorBits(previousCipher, lowNibble);
            String substitutedLow = substitute(xoredLow);
            previousCipher = substitutedLow;
            String combined = substitutedHigh + substitutedLow;
            ciphertext[i] = (byte) Integer.parseInt(combined, 2);
        }
        return ciphertext;
    }

    private static String xorBits(String a, String b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char bitA = a.charAt(i);
            char bitB = b.charAt(i);
            sb.append(bitA == bitB ? '0' : '1');
        }
        return sb.toString();
    }

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

        byte[] ecbCipher = ecbEncrypt(plaintext);
        byte[] ctrCipher = ctrEncrypt(ivHex, plaintext);
        byte[] cbcCipher = cbcEncrypt(ivBinary, plaintext);

        System.out.println("Plaintext: " + plaintextHex.toLowerCase());
        System.out.println("ECB Ciphertext: " + byteArrayToHexString(ecbCipher).toLowerCase());
        System.out.println("CTR Ciphertext: " + byteArrayToHexString(ctrCipher).toLowerCase());
        System.out.println("CBC Ciphertext: " + byteArrayToHexString(cbcCipher).toLowerCase());

        scanner.close();
    }
}
