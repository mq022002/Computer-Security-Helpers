import java.util.Scanner;

public class MainProgram {

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

    public static byte[] cbcEncrypt(String iv, byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        String previousCipher = iv;
        for (int i = 0; i < plaintext.length; i++) {
            byte currentByte = plaintext[i];
            String highNibble = String.format("%4s", Integer.toBinaryString((currentByte >> 4) & 0x0F)).replace(' ',
                    '0');
            String lowNibble = String.format("%4s", Integer.toBinaryString(currentByte & 0x0F)).replace(' ', '0');
            String xoredHigh = xorBits(previousCipher, highNibble);
            String substitutedHigh = SubstitutionCipher.substitute(xoredHigh);
            previousCipher = substitutedHigh;
            String xoredLow = xorBits(previousCipher, lowNibble);
            String substitutedLow = SubstitutionCipher.substitute(xoredLow);
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

        byte[] ecbCipher = ECBModeEncryption.ecbEncrypt(plaintext);
        byte[] ctrCipher = CTRModeEncryption.ctrEncrypt(ivHex, plaintext);
        byte[] cbcCipher = cbcEncrypt(ivBinary, plaintext);

        System.out.println("Plaintext: " + plaintextHex.toLowerCase());
        System.out.println("ECB Ciphertext: " + byteArrayToHexString(ecbCipher).toLowerCase());
        System.out.println("CTR Ciphertext: " + byteArrayToHexString(ctrCipher).toLowerCase());
        System.out.println("CBC Ciphertext: " + byteArrayToHexString(cbcCipher).toLowerCase());

        scanner.close();
    }
}
