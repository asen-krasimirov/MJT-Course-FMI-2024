package bg.sofia.uni.fmi.mjt.recipe.field;

public enum DishType {

    ALCOHOL_COCKTAIL("alcohol cocktail"),

    BISCUITS_AND_COOKIES("biscuits and cookies"),

    BREAD("bread"),

    CEREALS("cereals"),

    CONDIMENTS_AND_SAUCES("condiments and sauces"),

    DESSERTS("desserts"),

    DRINKS("drinks"),

    EGG("egg"),

    ICE_CREAM_AND_CUSTARD("ice cream and custard"),

    MAIN_COURSE("main course"),

    PANCAKE("pancake"),

    PASTA("pasta"),

    PASTRY("pastry"),

    PIES_AND_TARTS("pies and tarts"),

    PIZZA("pizza"),

    PREPS("preps"),

    PRESERVE("preserve"),

    SALAD("salad"),

    SANDWICHES("sandwiches"),

    SEAFOOD("seafood"),

    SIDE_DISH("side dish"),

    SOUP("soup"),

    SPECIAL_OCCASIONS("special occasions"),

    STARTER("starter"),

    SWEETS("sweets"),

    UNKNOWN("unknown");

    private final String value;

    DishType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidValue(String string) {
        for (DishType dishType : DishType.values()) {
            if (dishType.name().equals(string)) {
                return true;
            }
        }

        return false;
    }

}
