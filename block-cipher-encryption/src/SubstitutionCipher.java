public class SubstitutionCipher {

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
}
