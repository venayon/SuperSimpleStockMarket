package com.jp.ib.service.impl;

import com.jp.ib.dao.StockDao;
import com.jp.ib.entity.Stock;
import com.jp.ib.entity.util.StockType;
import com.jp.ib.execption.StockServiceException;
import com.jp.ib.service.StockService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.jp.ib.util.StockServiceConstants.SCALE;
import static com.jp.ib.util.ValidateUtils.notNull;
import static com.jp.ib.util.ValidateUtils.isGreaterThenZero;

public class StockServiceImpl implements StockService {
    private final StockDao stockDao;

    public StockServiceImpl(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public BigDecimal calculateDividendYield(final String stockSymbol, final BigDecimal stockPrice) throws StockServiceException {
        validate(stockSymbol, stockPrice);
        Optional<Stock> stock = stockDao.getStock(stockSymbol);
        if (Objects.isNull(stock) || !stock.isPresent()) {
            throw new StockServiceException("No stock present for symbol: " + stockSymbol);
        }
        return commputeDividend(stock).divide(stockPrice, SCALE, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public BigDecimal calculatePriceEarningsRatio(String stockSymbol, BigDecimal stockPrice) throws StockServiceException {
        notNull(stockPrice, "The stock price must not be null");
        isGreaterThenZero(stockPrice, "Stock Price must be Greater then Zero");
        //price/dividendYield
        Optional<Stock> stock = stockDao.getStock(stockSymbol);
        if (Objects.isNull(stock) || !stock.isPresent()) {
            throw new StockServiceException("No stock present for symbol: " + stockSymbol);
        }
        return stockPrice.divide(commputeDividend(stock),SCALE, BigDecimal.ROUND_HALF_UP).setScale(SCALE);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.getAll();
    }

    private BigDecimal commputeDividend(final Optional<Stock> stock) {
        final Stock stock1 = stock.get();

        StockType stockType = stock1.getStockType();

        if(Objects.isNull(stockType)){
            throw new RuntimeException("Invalid Stock type received : " + stockType);
        }

        switch (stockType) {
            case COMMON:
                return stock1.getLastDividend();
            case PREFERRED:
                return stock1.getFixedDividend().multiply(stock1.getParValue());
            default:
                throw new RuntimeException("Invalid Stock type received : " + stockType);
        }
    }

    private void validate(String stockSymbol, BigDecimal stockPrice) {
        notNull(stockSymbol, "The stockSymbol must not be null");
        notNull(stockPrice, "The stockprice must not be null");
        isGreaterThenZero(stockPrice, "Stock Price must be Greater then Zero");
    }
}
