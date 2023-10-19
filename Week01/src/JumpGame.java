import java.util.Arrays;

public class JumpGame {
    public static boolean canWin(int[] array) {
        if (array.length == 1 && array[0] != 0) {
            return true;
        }
        else if (array.length == 0) {
            return true;
        }

        int distance = 1;
        for (int i = array.length - 2; i >= 0; --i) {
            if (array[i] >= distance) {
                return canWin(Arrays.copyOfRange(array, 0, i));
            }

            distance++;
        }

        return false;
    }
}
