public class BrokenKeyboard {
    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        String[] words = message.split(" ");
        int canWriteCtr = 0;

        for (String word: words) {
            if (word.isEmpty()) continue;
            boolean canBeWritten = true;

            for (int i = 0; i < word.length(); ++i) {
                if (brokenKeys.contains(word.charAt(i) + "")) {
                    canBeWritten = false;
                    break;
                };
            }

            if (canBeWritten) canWriteCtr++;
        }

        return canWriteCtr;
    }
}
