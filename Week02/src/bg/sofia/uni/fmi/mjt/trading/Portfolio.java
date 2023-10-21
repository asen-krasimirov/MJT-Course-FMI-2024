package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Portfolio implements PortfolioAPI {
    private String owner;
    private PriceChartAPI priceChart;
    private double budget;
    private int maxSize;
    private int size;
    public StockPurchase[] stockPurchases;


    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.size = 0;
        this.stockPurchases = new StockPurchase[maxSize];
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize) {
        this.owner = owner;
        this.priceChart = priceChart;
        this.budget = budget;
        this.maxSize = maxSize;
        this.size = stockPurchases.length;
        this.stockPurchases = new StockPurchase[maxSize];
        System.arraycopy(stockPurchases, 0, this.stockPurchases, 0, size);
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (stockTicker == null) return null;
        if (size >= maxSize) return null;
        if (quantity < 0) return null;

        double currentStockPrice = priceChart.getCurrentPrice(stockTicker);
        double hasToPay = currentStockPrice * quantity;
        if (hasToPay > getRemainingBudget()) return null;

        StockPurchase purchase = null;

        switch (stockTicker) {
            case "MSFT" -> purchase = new MicrosoftStockPurchase(quantity, LocalDateTime.now(), currentStockPrice);
            case "GOOG" -> purchase = new GoogleStockPurchase(quantity, LocalDateTime.now(), currentStockPrice);
            case "AMZ" -> purchase = new AmazonStockPurchase(quantity, LocalDateTime.now(), currentStockPrice);
            default -> {
                return null;
            }
        }

        budget -= hasToPay;
        stockPurchases[size++] = purchase;
        priceChart.changeStockPrice(stockTicker, 5);

        return purchase;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return Arrays.copyOf(stockPurchases, size);
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        StockPurchase[] stockPurchasesToReturn = new StockPurchase[size];

        int count = 0;
        for (int i = 0; i < size; ++i) {
            if (stockPurchases[i].getPurchaseTimestamp().equals(startTimestamp) ||
                    stockPurchases[i].getPurchaseTimestamp().equals(endTimestamp) ||
                    (stockPurchases[i].getPurchaseTimestamp().isAfter(startTimestamp) && stockPurchases[i].getPurchaseTimestamp().isBefore(endTimestamp))
            ) {
                stockPurchasesToReturn[i] = stockPurchases[i];
                count++;
            }
        }

        return Arrays.copyOf(stockPurchasesToReturn, count);
    }

    @Override
    public double getNetWorth() {
        double sum = 0.0;

        for (int i = 0; i < size; ++i) {
            sum += stockPurchases[i].getTotalPurchasePrice();
        }

        return Math.round(sum * 100.0) / 100.0;
    }

    @Override
    public double getRemainingBudget() {
        return Math.round(budget * 100.0) / 100.0;
    }

    @Override
    public String getOwner() {
        return owner;
    }

}
