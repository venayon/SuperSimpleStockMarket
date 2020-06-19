package com.jp.ib.service;

import com.jp.ib.entity.Trade;

import java.math.BigDecimal;
import java.util.List;

public interface TradeService {
    Trade buy(String stockSymbol, BigDecimal quantity, BigDecimal price, long timeStamp);

    Trade sell(String stockSymbol, BigDecimal quantity, BigDecimal price, long timeStamp);

    BigDecimal calculateVolumeWeightPrice(String symbol, long currentTime);

    BigDecimal calculateVolumeWeightPrice(String symbol);


}
