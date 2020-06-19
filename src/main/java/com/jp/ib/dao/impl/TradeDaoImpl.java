package com.jp.ib.dao.impl;

import com.jp.ib.dao.TradeDao;
import com.jp.ib.entity.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TradeDaoImpl implements TradeDao {
    private final List<Trade> trades = new ArrayList<>();

    @Override
    public boolean recordTrade(Trade trade) {
        return trades.add(trade);
    }

    @Override
    public List<Trade> findByStock(final String stockSymbol) {
        return trades.parallelStream()
                .filter(t->stockSymbol.equalsIgnoreCase(t.getStockSymbol()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Trade> findByStocks(final List<String> stockSymbolList) {
        return trades.parallelStream().filter(trade -> stockSymbolList.contains(trade.getStockSymbol())).collect(Collectors.toList());
    }
}
