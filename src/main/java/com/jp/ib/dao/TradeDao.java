package com.jp.ib.dao;

import com.jp.ib.entity.Trade;

import java.util.List;

public interface TradeDao {
    boolean recordTrade(Trade t);
    List<Trade> findByStock(String stockSymbol);

    List<Trade> findByStocks(List<String> stockSymbolList);
}
