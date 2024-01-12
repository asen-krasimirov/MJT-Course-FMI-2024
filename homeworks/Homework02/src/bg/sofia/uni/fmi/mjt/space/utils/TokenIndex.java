package bg.sofia.uni.fmi.mjt.space.utils;

public enum TokenIndex {
    INDEX_ZERO(0),
    INDEX_ONE(1),
    INDEX_TWO(2),
    INDEX_THREE(3),
    INDEX_FOUR(4),
    INDEX_FIVE(5),
    INDEX_SIX(6),
    INDEX_SEVEN(7);

    private final int index;

    TokenIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
