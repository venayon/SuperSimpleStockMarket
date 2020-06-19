package com.jp.ib.dao.impl;

import com.jp.ib.dao.StockDao;
import com.jp.ib.entity.Stock;
import com.jp.ib.entity.util.StockType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stock Symbol  | Type | Last Dividend | Fixed Dividend | Par Value
 * ------------- | ---- | ------------: | :------------: | --------:
 * TEA          Common	        0          		                100
 * POP	        Common	        8       		                100
 * ALE	        Common	        23      		                60
 * GIN	        Preferred	    8       	2%  	            100
 * JOE	        Common	        13		                        250
 */
public class StockDaoImpl implements StockDao {

    private static final List<Stock> stocks = new ArrayList<Stock>() {
        {
            add(new Stock("TEA", StockType.COMMON, BigDecimal.ZERO, null, new BigDecimal("1.00")));
            add(new Stock("POP", StockType.COMMON, new BigDecimal("0.08"), null, new BigDecimal("1.00")));
            add(new Stock("ALE", StockType.COMMON, new BigDecimal("0.23"), null, new BigDecimal("0.60")));
            add(new Stock("GIN", StockType.PREFERRED, new BigDecimal("0.08"), new BigDecimal("0.02"), new BigDecimal("1.0")));
            add(new Stock("JOE", StockType.COMMON, new BigDecimal("0.13"), null, new BigDecimal("2.50")));
        }
    };

    public Optional<Stock> getStock(final String stockSymbol) {
        return stocks.stream().filter(s -> s.getStockSymbol().equalsIgnoreCase(stockSymbol)).findFirst();
    }

    @Override
    public List<Stock> getAll() {
        return new ArrayList<>(stocks);
    }
}
