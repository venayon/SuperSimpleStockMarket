package com.jp.ib.entity;

import com.jp.ib.entity.util.Indicator;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
    private final String stockSymbol;
    private final Indicator indicator;
    private final BigDecimal price;
    private final BigDecimal quantity;
    private final long timeStamp;

    public Trade(String stockSymbol, Indicator indicator, BigDecimal quantity, BigDecimal price, long timeStamp) {
        this.stockSymbol = stockSymbol;
        this.indicator = indicator;
        this.price = price;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trade{");
        sb.append("stockSymbol='").append(stockSymbol).append('\'');
        sb.append(", indicator=").append(indicator);
        sb.append(", price=").append(price);
        sb.append(", quantity=").append(quantity);
        sb.append(", timeStamp=").append(new Date(timeStamp)); // Not required
        sb.append('}');
        return sb.toString();
    }
}
