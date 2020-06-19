package com.jp.ib.service.impl;

import com.jp.ib.entity.Stock;
import com.jp.ib.service.GlobalBeverageCorporationExchange;
import com.jp.ib.service.StockService;
import com.jp.ib.service.TradeService;
import com.jp.ib.util.StockServiceConstants;
import org.apache.commons.math3.stat.StatUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalBeverageCorporationExchangeImpl implements GlobalBeverageCorporationExchange {

    private StockService stockService;
    private TradeService tradeService;

    public GlobalBeverageCorporationExchangeImpl(StockService stockService, TradeService tradeService) {
        this.stockService = stockService;
        this.tradeService = tradeService;
    }

    @Override
    public BigDecimal calculateAllShareIndex() {
        List<Stock> stocks = stockService.getAllStocks();

        List<BigDecimal> volumeWeightedStockPrices = stocks.parallelStream()
                .map(s -> tradeService.calculateVolumeWeightPrice(s.getStockSymbol())).collect(Collectors.toList());

        double[] stockPrices = new double[volumeWeightedStockPrices.size()];

        for (int i = 0; i < volumeWeightedStockPrices.size(); i++) {
            BigDecimal value = volumeWeightedStockPrices.get(i);
            if (value != null && value.compareTo(BigDecimal.ZERO) != 0)
                stockPrices[i] = value.doubleValue();
        }
        return BigDecimal.valueOf(StatUtils.geometricMean(stockPrices)).setScale(StockServiceConstants.SCALE);
    }
}
