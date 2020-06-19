package com.jp.ib;

import com.jp.ib.entity.Trade;
import com.jp.ib.entity.util.Indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

final public class MockTrades {

    private static final Logger LOGGER = Logger.getLogger(MockTrades.class.getName());

    public static List<Trade> recordTrads(long currentTimeMillis) {
        String[] tickers = {"TEA", "POP", "ALE", "GIN", "JOE"};
        int seconds = 16 * 1000;
        long past5mins = currentTimeMillis - (5 * 60 * 1000);
        long currentTimeMillisPlus = currentTimeMillis + (3 * 60 * 1000);

        List<Trade> tradeList = new ArrayList<>();
        LOGGER.log(Level.INFO, "Trade booking start time:{0}", new Date(past5mins));
        LOGGER.log(Level.INFO, "Trade booking close time:{0}", new Date(currentTimeMillisPlus));

        boolean changeInd = false;
        for (long step = past5mins; step < currentTimeMillisPlus; step += seconds) {
            Indicator indicator = changeInd ? Indicator.BUY : Indicator.SELL;
            for (String symbol : tickers) {
                tradeList.add(new Trade(symbol, indicator, BigDecimal.TEN, BigDecimal.valueOf(0.2), step));
            }
            changeInd = !changeInd;
        }
        // -T<T>+T
        tradeList.stream().filter(t -> "JOE".equals(t.getStockSymbol()))
                .filter(t -> t.getStockSymbol().equalsIgnoreCase("JOE"))
                .filter(t -> t.getTimeStamp() >= past5mins && t.getTimeStamp() <= currentTimeMillis)
                //  .peek(t -> System.out.println(new Date(t.getTimeStamp())))
                .close();
        assertEquals("Mock trades not created correctly ", 150, tradeList.size());
        assertEquals(tradeList.stream().filter(t -> Indicator.BUY.equals(t.getIndicator())).count(), tradeList.stream().filter(t -> Indicator.SELL.equals(t.getIndicator())).count());
        return tradeList;
    }
}
