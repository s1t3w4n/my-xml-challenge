import java.util.HashMap;
import java.util.Map;

public class htmlDecoder {
    private final static String symbols =
            "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";
    private final static int startSymbols = 1040;
    private final static int endSymbols = 1103;
    private final static char youLow = 'ё';
    private final static int numberYouLow = 1105;
    private final static char youHight = 'Ё';
    private final static int numberYouHight = 1025;
    private final static char coma = ',';
    private final static int numberComma = 44;
    private final static char dot = '.';
    private final static int numberDot = 46;
    private final static char semicolon = ';';
    private final static int numberSemicolon = 46;
    private final static char colon = ':';
    private final static int numberColon = 58;

    private final Map<Character, Integer> codes;
    private final Map<Integer, Character> characters;

    public htmlDecoder() {
        codes = new HashMap<>();
        characters = new HashMap<>();

        for (int i = startSymbols; i <= endSymbols; i++) {
            codes.put(symbols.charAt(i - startSymbols), i);
        }
        codes.put(youLow, numberYouLow);
        codes.put(youHight, numberYouHight);
        codes.put(coma, numberComma);
        //codes.put(dot, numberDot);
        codes.put(semicolon, numberSemicolon);
        codes.put(colon, numberColon);

        for (Map.Entry<Character, Integer> entry : codes.entrySet()) {
            characters.put(entry.getValue(), entry.getKey());
        }
    }

    String code(String text) {
        StringBuilder sb = new StringBuilder();
        StringBuilder cb = new StringBuilder();
        boolean switcher = false;

        for (char c : text.toCharArray()) {
            if (switcher) {
                if (c == '#') {
                    continue;
                } else if (Character.isDigit(c)) {
                    cb.append(c);
                } else {
                    if (characters.containsKey(Integer.parseInt(cb.toString()))) {
                        sb.append(characters.get(Integer.parseInt(cb.toString())));
                        cb.delete(0, cb.length());
                        switcher = false;
                    }
                }
            } else {
                if (c != '&') {
                    sb.append(c);
                } else {
                    switcher = true;
                }
            }
        }
        return sb.toString();
    }
}
