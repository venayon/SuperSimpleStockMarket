package com.jp.ib.entity;

import com.jp.ib.entity.util.StockType;

import java.math.BigDecimal;

public class Stock {



    private String stockSymbol;
    private StockType stockType;
    private BigDecimal lastDividend;
    private BigDecimal fixedDividend;
    private BigDecimal parValue;


    public Stock(String stockSymbol, StockType stockType, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue) {
        this.stockSymbol = stockSymbol;
        this.stockType = stockType;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public StockType getStockType() {
        return stockType;
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }

    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Stock{");
        sb.append("stockSymbol='").append(stockSymbol).append('\'');
        sb.append(", stockType=").append(stockType);
        sb.append(", lastDividend=").append(lastDividend);
        sb.append(", fixedDividend=").append(fixedDividend);
        sb.append(", parValue=").append(parValue);
        sb.append('}');
        return sb.toString();
    }
}
