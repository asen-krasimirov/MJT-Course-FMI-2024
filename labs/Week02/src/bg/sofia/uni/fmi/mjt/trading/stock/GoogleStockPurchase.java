package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public class GoogleStockPurchase extends AbstractStockPurchase {
    private static final String STOCK_TICKER = "GOOG";

    public GoogleStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        super(quantity, purchaseTimestamp, purchasePricePerUnit);
    }
    @Override
    public String getStockTicker() {
        return STOCK_TICKER;
    }

}
