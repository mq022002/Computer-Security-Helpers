/**
 * This class provides a method for substituting a four-bit binary string with
 * its corresponding character from
 * the substitution table.
 *
 * @author Matthew Quijano
 */
public class SubstitutionCipher {

    /**
     * The substitution table used for block cipher encryption.
     * Each element in the array represents a substitution value for a 4-bit input.
     * The substitution values are binary strings of length 4.
     */
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

    /**
     * Substitutes a four-bit binary string with its corresponding character from
     * the substitution table.
     *
     * @param fourBits the four-bit binary string to be substituted
     * @return the character corresponding to the four-bit binary string
     */
    public static String substitute(String fourBits) {
        int index = Integer.parseInt(fourBits, 2);
        return SUBSTITUTION[index];
    }
}