public class BrokenKeyboard {
    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        String[] words = message.split(" ");
        int canWriteCtr = words.length;

        for (String word: words) {
            if (word.isEmpty() || word.isBlank()) canWriteCtr--;

            for (int i = 0; i < word.length(); ++i) {
                if (brokenKeys.contains(word.charAt(i) + "")) {
                    canWriteCtr--;
                    break;
                };
            }

        }

        return canWriteCtr;
    }
}
