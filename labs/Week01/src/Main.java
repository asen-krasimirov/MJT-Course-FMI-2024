public class Main {
    public static void exercise1Tests() {
        System.out.println(IPValidator.validateIPv4Address("192.168.1.1"));     // true
        System.out.println(IPValidator.validateIPv4Address("192.168.1.0"));     // true
        System.out.println(IPValidator.validateIPv4Address("192.168.1.00"));    // false
        System.out.println(IPValidator.validateIPv4Address("192.168@1.1"));     // false
        System.out.println(IPValidator.validateIPv4Address("192.168@.1.1"));    // false
        System.out.println(IPValidator.validateIPv4Address("192.168.256.1"));   // false
        System.out.println(IPValidator.validateIPv4Address(".192.168.2561"));   // false
    }
    public static void exercise2Tests() {
        System.out.println(JumpGame.canWin(new int[]{2, 3, 1, 1, 0}));   // true;
        System.out.println(JumpGame.canWin(new int[]{4, 0, 0, 0, 0}));   // true;
        System.out.println(JumpGame.canWin(new int[]{3, 0, 2, 0}));      // true;
        System.out.println(JumpGame.canWin(new int[]{3, 2, 1, 0, 0}));   // false;
        System.out.println(JumpGame.canWin(new int[]{0, 2, 1, 0, 0}));   // false;
    }

    public static void exercise3Tests() {
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("i love mjt", "qsf3o"));                     // 2
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("secret      message info      ", "sms"));   // 1
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("dve po 2 4isto novi beli kecove", "o2sf")); // 2
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("     ", "asd"));                            // 0
        System.out.println(BrokenKeyboard.calculateFullyTypedWords(" - 1 @ - 4", "s"));                         // 5
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("Helly World", "l"));                        // 0
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("Helly      World   A ", "o"));              // 2
        System.out.println(BrokenKeyboard.calculateFullyTypedWords("А  ", "o"));                                // 1
    }

    public static void main(String[] args) {
//        exercise1Tests();
//        exercise2Tests();
        exercise3Tests();
    }
}