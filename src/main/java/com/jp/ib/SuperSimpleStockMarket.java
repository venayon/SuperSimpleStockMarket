package com.jp.ib;

import com.jp.ib.entity.Trade;
import com.jp.ib.entity.util.Indicator;
import com.jp.ib.execption.StockServiceException;
import com.jp.ib.service.StockService;
import com.jp.ib.service.TradeService;

import java.math.BigDecimal;

import static com.jp.ib.util.ValidateUtils.notNull;
import static com.jp.ib.util.ValidateUtils.isGreaterThenZero;


public class SuperSimpleStockMarket {

    public static final String THE_STOCK_SYMBOL_MUST_NOT_BE_NULL = "The stockSymbol must not be null";
    public static final String THE_STOCKPRICE_MUST_NOT_BE_NULL = "The stockprice must not be null";
    public static final String STOCK_PRICE_MUST_BE_GREATER_THEN_ZERO = "Stock Price must be Greater then Zero";

    final private StockService stockService;
    final private TradeService tradeService;

    public SuperSimpleStockMarket(StockService stockService, TradeService tradeService) {
        this.stockService = stockService;
        this.tradeService = tradeService;
    }

    /**
     * Given any price as input, calculate the dividend yield
     *
     * @param stockSymbol
     * @param stockPrice
     * @return
     * @throws StockServiceException
     */

    public BigDecimal calculateDividendYield(String stockSymbol, BigDecimal stockPrice) throws StockServiceException {
        validateInputArguments(stockSymbol, stockPrice);
        return stockService.calculateDividendYield(stockSymbol, stockPrice);
    }

    /**
     * Given any price as input, calculate the P/E Ratio
     *
     * @param stockSymbol
     * @param stockPrice
     * @return
     * @throws StockServiceException
     */
    public BigDecimal calculatePriceEarningsRatio(String stockSymbol, BigDecimal stockPrice) throws StockServiceException {
        validateInputArguments(stockSymbol, stockPrice);
        return stockService.calculatePriceEarningsRatio(stockSymbol, stockPrice);
    }

    /**
     *  Calculate Volume Weighted Stock Price based on trades in past  5 minutes
     * @param symbol
     * @param currentTime
     * @return
     */
    public BigDecimal computePastFiveMinuteVolumeWeightPrice(String symbol,long currentTime) throws StockServiceException {
        notNull(symbol, THE_STOCK_SYMBOL_MUST_NOT_BE_NULL);
        return tradeService.calculateVolumeWeightPrice(symbol,currentTime);
    }


    public void recordTrade(String stockSymbol, String tradeIndicator, BigDecimal quantity, BigDecimal price, long timeStamp) throws StockServiceException {
        notNull(stockSymbol, THE_STOCK_SYMBOL_MUST_NOT_BE_NULL);
        isGreaterThenZero(quantity, "Share quantity must be greater than zero");
        isGreaterThenZero(price, STOCK_PRICE_MUST_BE_GREATER_THEN_ZERO);
        notNull(tradeIndicator, " Trade indicator must not be null ");
        Indicator indicator = Indicator.valueOf(tradeIndicator);
        notNull(indicator,"Invalid Trade type");
       switch (indicator){
           case BUY:
               tradeService.buy(stockSymbol,quantity,price,timeStamp);
               break;
           case SELL:
               tradeService.sell(stockSymbol,quantity,price,timeStamp);
               break;
               default:
       }
    }

    /**
     * class scoped : to validate specific input parameters (stock symbol and price) from caller,
     * which also eliminates code duplication
     * @param stockSymbol
     * @param stockPrice
     */
    private void validateInputArguments(String stockSymbol, BigDecimal stockPrice) {
        notNull(stockSymbol, THE_STOCK_SYMBOL_MUST_NOT_BE_NULL);
        notNull(stockPrice, THE_STOCKPRICE_MUST_NOT_BE_NULL);
        isGreaterThenZero(stockPrice, STOCK_PRICE_MUST_BE_GREATER_THEN_ZERO);
    }
}
