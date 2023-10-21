import bg.sofia.uni.fmi.mjt.trading.Portfolio;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;

public class Main {
    private static final double EPSILON = 0.001;

    public static void testPriceChartClass() {
        boolean isCorrect = true;

        PriceChart test = new PriceChart(1.1, 2.2, 3.3);

        test.changeStockPrice("GOOG", 50);

        if (!(Math.abs(test.getCurrentPrice("GOOG") - 3.3) < EPSILON)) isCorrect = false;

        System.out.println("Test #1: " + (isCorrect ? "Correct" : "Invalid"));
    }
    public static void testSpecificStockPurchaseClass() {
        boolean isCorrect = true;

        var timeNow = LocalDateTime.now();

        MicrosoftStockPurchase test = new MicrosoftStockPurchase(7, timeNow, 2.51);

        if (!(test.getQuantity() == 7)) isCorrect = false;
        else if (!(test.getPurchaseTimestamp().equals(timeNow))) isCorrect = false;
        else if (!(Math.abs(test.getPurchasePricePerUnit() - 2.51) < EPSILON)) isCorrect = false;
        else if (!(Math.abs(test.getTotalPurchasePrice() - 17.57) < EPSILON)) isCorrect = false;
        else if (!(test.getStockTicker().equals("MSFT"))) isCorrect = false;

        System.out.println("Test #2: " + (isCorrect ? "Correct" : "Invalid"));
    }

    public static void testPortfolioConstructor() {
        var timeNow = LocalDateTime.now();
        var priceChart = new PriceChart(1.1, 2.2, 3.3);

        StockPurchase[] stockPurchases = { new MicrosoftStockPurchase(1, timeNow, 1.1), new AmazonStockPurchase(2, timeNow, 2.2)};

        Portfolio test = new Portfolio("Pesho", priceChart, stockPurchases, 100, 5);

        System.out.println(Arrays.equals(test.stockPurchases, stockPurchases)); // not sure what wanted behaviour is
    }

    public static void testEmptyPortfolio() {
        var timeNow = LocalDateTime.now();
        var priceChart = new PriceChart(1.1, 2.2, 3.3);

        Portfolio test = new Portfolio("Pesho", priceChart, 100, 5);

        System.out.println(test.getNetWorth());
    }

    public static void testBudgetGoesToZero() {
        var timeNow = LocalDateTime.now();
        var priceChart = new PriceChart(1, 2, 3);

        Portfolio test = new Portfolio("Pesho", priceChart, 10, 5);

        test.buyStock("GOOG", 5);

        System.out.println(test.getRemainingBudget());
        System.out.println(priceChart.getCurrentPrice("GOOG"));

    }

    public static void testTimestampedPurchases() {
        var priceChart = new PriceChart(1, 2, 3);
        Portfolio test = new Portfolio("Pesho", priceChart, 100, 5);

        test.buyStock("GOOG", 5);
        test.buyStock("GOOG", 5);
        test.buyStock("GOOG", 5);

        LocalDateTime startTime = LocalDateTime.of(2023, 10, 22, 1, 10);
        LocalDateTime endTime = LocalDateTime.of(2023, 10, 22, 1, 15);

        System.out.println(Arrays.toString(test.getAllPurchases(startTime, endTime)));

    }

    public static void testNetWorth1() {
        var priceChart = new PriceChart(1, 10, 3);
        Portfolio test = new Portfolio("Pesho", priceChart, 1000, 10);

        for (int i = 0; i < 6; ++i) {
            test.buyStock("GOOG", 10);
            System.out.println("Net Worth: " + test.getNetWorth());
            System.out.println("Budget: " + test.getRemainingBudget());
        }
    }

    public static void testNetWorth2() {
        var priceChart = new PriceChart(1, 1, 3);
        Portfolio test = new Portfolio("Pesho", priceChart, 100, 10);

        for (int i = 0; i < 6; ++i) {
            test.buyStock("GOOG", 10);
            System.out.println("Net Worth: " + test.getNetWorth());
            System.out.println("Budget: " + test.getRemainingBudget());
        }
    }

    public static void main(String[] args) {
//        testPriceChartClass();
//        testSpecificStockPurchaseClass();

//        testPortfolioConstructor();
//        testEmptyPortfolio();
//        testBudgetGoesToZero();
//        testTimestampedPurchases();
        testNetWorth1();
//        testNetWorth2();
    }
}