public class CTRModeEncryption {

    public static byte[] ctrEncrypt(String iv, byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        int counter = Integer.parseInt(iv, 16) & 0x0F;
        for (int i = 0; i < plaintext.length; i++) {
            byte currentByte = plaintext[i];
            String highNibble = String.format("%4s", Integer.toBinaryString((currentByte >> 4) & 0x0F)).replace(' ',
                    '0');
            String lowNibble = String.format("%4s", Integer.toBinaryString(currentByte & 0x0F)).replace(' ', '0');

            String counterBinary = String.format("%4s", Integer.toBinaryString(counter)).replace(' ', '0');
            String encryptedCounterHigh = SubstitutionCipher.substitute(counterBinary);
            String encryptedHigh = xorBits(encryptedCounterHigh, highNibble);
            counter = (counter + 1) & 0x0F;

            counterBinary = String.format("%4s", Integer.toBinaryString(counter)).replace(' ', '0');
            String encryptedCounterLow = SubstitutionCipher.substitute(counterBinary);
            String encryptedLow = xorBits(encryptedCounterLow, lowNibble);
            counter = (counter + 1) & 0x0F;

            String combined = encryptedHigh + encryptedLow;
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
