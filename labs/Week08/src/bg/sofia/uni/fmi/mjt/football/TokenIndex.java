package bg.sofia.uni.fmi.mjt.football;

public enum TokenIndex {

    INDEX_ZERO(0),
    INDEX_ONE(1),
    INDEX_TWO(2),
    INDEX_THREE(3),
    INDEX_FOUR(4),
    INDEX_FIVE(5),
    INDEX_SIX(6),
    INDEX_SEVEN(7),
    INDEX_EIGHT(8),
    INDEX_NINE(9),
    INDEX_TEN(10),
    INDEX_ELEVEN(11),
    INDEX_TWELVE(12);

    private final int index;

    TokenIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
