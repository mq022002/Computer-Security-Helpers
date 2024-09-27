/**
 * This class provides a method for encrypting plaintext using the ECB
 * (Electronic Codebook) mode of encryption.
 *
 * @author Matthew Quijano
 */
public class ECBModeEncryption {

    /**
     * Encrypts the given plaintext using the ECB (Electronic Codebook) mode of
     * encryption.
     *
     * @param plaintext the plaintext to be encrypted
     * @return the ciphertext obtained after encryption
     */
    public static byte[] ecbEncrypt(byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        for (int i = 0; i < plaintext.length; i++) {
            byte currentByte = plaintext[i];
            String highNibble = String.format("%4s", Integer.toBinaryString((currentByte >> 4) & 0x0F)).replace(' ',
                    '0');
            String lowNibble = String.format("%4s", Integer.toBinaryString(currentByte & 0x0F)).replace(' ', '0');
            String substitutedHigh = SubstitutionCipher.substitute(highNibble);
            String substitutedLow = SubstitutionCipher.substitute(lowNibble);
            String combined = substitutedHigh + substitutedLow;
            ciphertext[i] = (byte) Integer.parseInt(combined, 2);
        }
        return ciphertext;
    }
}