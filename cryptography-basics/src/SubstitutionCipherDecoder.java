import java.util.HashMap;
import java.util.Map;

public class SubstitutionCipherDecoder {
    public static void main(String[] args) {

        String text1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String text2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String cipher = text1;
        String alpha = text2;

        String ciphertext = "EQ VGYGLF 26, 2024 ZJQFDVS ZEQQJZFHZGF LFVFJ GQHOJDLHFB NVL IJLHYQVFJI VL V QVFHEQVS ZJQFJD ET VZVIJXHZ JKZJSSJQZJ HQ ZBPJD EUJDVFHEQL (ZVJ-ZE) PB FMJ QVFHEQVS LJZGDHFB VYJQZB (QLV), XVCHQY ZZLG RGLF FMJ 22QI HQLFHFGFHEQ HQ FMJ ZEGQFDB FE JVDQ FMVF IHLFHQZFHEQ.";

        Map<Character, Character> cipherMap = new HashMap<>();
        for (int i = 0; i < cipher.length(); i++) {
            cipherMap.put(alpha.charAt(i), cipher.charAt(i));
        }

        StringBuilder plaintext = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            if (Character.isUpperCase(c) && cipherMap.containsKey(c)) {
                char mappedChar = cipherMap.get(c);
                if (Character.isLowerCase(mappedChar)) {
                    plaintext.append(mappedChar);
                } else {
                    plaintext.append(Character.toLowerCase(mappedChar));
                }
            } else if (Character.isLowerCase(c)) {
                plaintext.append(c);
            } else {
                plaintext.append(c);
            }
        }

        System.out.println("\n" + plaintext.toString() + "\n");
    }
}
