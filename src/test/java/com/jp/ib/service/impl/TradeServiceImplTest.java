package com.jp.ib.service.impl;

import com.jp.ib.MockTrades;
import com.jp.ib.dao.TradeDao;
import com.jp.ib.entity.Trade;
import com.jp.ib.entity.util.Indicator;
import com.jp.ib.execption.StockServiceException;
import com.jp.ib.service.TradeService;
import org.hamcrest.number.BigDecimalCloseTo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TradeServiceImplTest {
    private static final Logger LOGGER = Logger.getLogger(TradeServiceImplTest.class.getName());
    private static TradeDao tradeDao;
    private TradeService tradeService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        tradeDao = mock(TradeDao.class);
        tradeService = new TradeServiceImpl(tradeDao);
    }

    @Test
    public void testComputeFiveMinuteVolumeWeightPrice() {
        final String symbol = "GIN";

        long currentTimeMillis = System.currentTimeMillis();
        when(tradeDao.findByStock(symbol)).thenReturn(MockTrades.recordTrads(currentTimeMillis)
                .parallelStream()
                .filter(t -> symbol.equalsIgnoreCase(t.getStockSymbol()))
                .collect(Collectors.toList()));
        LOGGER.log(Level.INFO, "VolumeWeightPrice for past 5 mins:{0} ", tradeService.calculateVolumeWeightPrice(symbol, currentTimeMillis));
        BigDecimal ERROR = BigDecimal.ZERO;
        assertThat("", tradeService.calculateVolumeWeightPrice(symbol, currentTimeMillis), BigDecimalCloseTo.closeTo(BigDecimal.valueOf(0.2), ERROR));
    }

    @Test
    public void testComputeFiveMinuteVolumeWeightPrice_invalid_stockSybmol_failure() throws StockServiceException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Stock symbol should not be null or empty");
        tradeService.calculateVolumeWeightPrice("", 1);
    }

    @Test
    public void test_buy() {
        long timeStamp = System.currentTimeMillis();
        Trade trade = tradeService.buy("TEA", BigDecimal.TEN, BigDecimal.ONE, timeStamp);
        // Trade trade = tradeService.buy(null, null,null,timeStamp);
        assertEquals(Indicator.BUY, trade.getIndicator());
        assertEquals(timeStamp, trade.getTimeStamp());
        assertEquals(timeStamp, trade.getTimeStamp());
        assertEquals("TEA", trade.getStockSymbol());
        assertEquals(BigDecimal.TEN, trade.getQuantity());
        assertEquals(BigDecimal.ONE, trade.getPrice());
    }

    @Test
    public void test_sell() {
        long timeStamp = System.currentTimeMillis();
        Trade trade = tradeService.sell("TEA", BigDecimal.TEN, BigDecimal.ONE, timeStamp);
        assertEquals(Indicator.SELL, trade.getIndicator());
        assertEquals(timeStamp, trade.getTimeStamp());
        assertEquals("TEA", trade.getStockSymbol());
        assertEquals(BigDecimal.TEN, trade.getQuantity());
        assertEquals(BigDecimal.ONE, trade.getPrice());
    }
}