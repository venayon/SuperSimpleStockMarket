package com.jp.ib.service;

import com.jp.ib.entity.Stock;
import com.jp.ib.execption.StockServiceException;

import java.math.BigDecimal;
import java.util.List;

public interface StockService {

    BigDecimal calculateDividendYield(String stockSymbol, BigDecimal stockPrice) throws StockServiceException;

    BigDecimal calculatePriceEarningsRatio(String stockSymbol, BigDecimal stockPrice) throws StockServiceException;

    List<Stock> getAllStocks();
}
