package com.jp.ib.dao.impl;

import com.jp.ib.dao.TradeDao;
import com.jp.ib.entity.Trade;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TradeDaoImplTest  {
    private TradeDao tradeDao;
    @Before
    public void setup(){
        tradeDao = new TradeDaoImpl();
    }

    @Test
    public  void test_findTradesByStock_success(){
        String stockSymbol = "STK";
        assertTrue(tradeDao.recordTrade(new Trade(stockSymbol,null,null,null,0L)));
        List<Trade> trade = tradeDao.findByStock(stockSymbol);
        assertTrue("No trades found for stock ",!trade.isEmpty());
    }
    @Test
    public  void test_findTradesByStock_no_trade_recorded_forstock_should_return_empty_success(){
        String stockSymbol = "APP";
        List<Trade> trade = tradeDao.findByStock(stockSymbol);
        assertTrue("trades found for stock ",trade.isEmpty());
    }
}