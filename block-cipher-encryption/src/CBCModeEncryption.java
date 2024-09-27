public class CBCModeEncryption {

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
}
