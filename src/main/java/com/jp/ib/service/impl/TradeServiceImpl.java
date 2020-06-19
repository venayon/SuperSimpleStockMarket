package com.jp.ib.service.impl;

import com.jp.ib.dao.TradeDao;
import com.jp.ib.entity.Trade;
import com.jp.ib.entity.util.Indicator;
import com.jp.ib.execption.StockServiceException;
import com.jp.ib.service.TradeService;
import com.jp.ib.util.ValidateUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jp.ib.util.StockServiceConstants.SCALE;
import static java.math.BigDecimal.ROUND_HALF_UP;

public class TradeServiceImpl implements TradeService {

    private static final Logger LOGGER  = Logger.getLogger( TradeServiceImpl.class.getName());
    private final TradeDao tradeDao;

    public TradeServiceImpl(TradeDao tradeDao) {
        this.tradeDao = tradeDao;
    }


    @Override
    public Trade buy(String stockSymbol, BigDecimal quantity, BigDecimal price, long timeStamp) {
        Trade trade = new Trade(stockSymbol, Indicator.BUY, quantity, price, timeStamp);
        tradeDao.recordTrade(trade);
        return trade;
    }

    @Override
    public Trade sell(String stockSymbol, BigDecimal quantity, BigDecimal price, long timeStamp) {
        Trade trade = new Trade(stockSymbol, Indicator.SELL, quantity, price, timeStamp);
        tradeDao.recordTrade(trade);
        return trade;
    }

    @Override
    public BigDecimal calculateVolumeWeightPrice(final String symbol, long currentTimeMillis) {
        long past5mins = currentTimeMillis - (5 * 60 * 1000);
        List<Trade> stockList = getTradeStream(symbol)
                .filter(t -> t.getTimeStamp() >= past5mins && t.getTimeStamp() <= currentTimeMillis)
                .peek(t -> logFine(t))
                .collect(Collectors.toList());

        return getBigDecimal(stockList);
    }

    private Stream<Trade> getTradeStream(String symbol) {
        ValidateUtils.notNull(symbol, "Stock symbol should not be null or empty");
        List<Trade> trades = tradeDao.findByStock(symbol);
        return Optional.ofNullable(trades).orElse(Collections.emptyList())
                .stream()
                .filter(t -> symbol.equals(t.getStockSymbol()));
    }

    @Override
    public BigDecimal calculateVolumeWeightPrice(String symbol) {
        List<Trade> stockList = getTradeStream(symbol).collect(Collectors.toList());
        return getBigDecimal(stockList);
    }

    private BigDecimal getBigDecimal(List<Trade> stockList) {
        BigDecimal stockTradedValue = BigDecimal.ZERO;
        BigDecimal totalStockQty = BigDecimal.ZERO;
        for (Trade t : stockList){
            BigDecimal product =  t.getPrice().multiply(t.getQuantity());
            stockTradedValue =   stockTradedValue.add(product);
            totalStockQty = totalStockQty.add(t.getQuantity());
        }
        LOGGER.log(Level.FINE,"stockTradedValue:{0} and totalStockQty:{1} ",new Object[]{stockTradedValue,totalStockQty});
        return totalStockQty.compareTo(BigDecimal.ZERO) != 0 ? stockTradedValue.divide(totalStockQty,SCALE,ROUND_HALF_UP) : BigDecimal.ZERO;
    }

    private void logFine(Trade t) {
        LOGGER.log(Level.INFO,"{0}",t);
    }
}
