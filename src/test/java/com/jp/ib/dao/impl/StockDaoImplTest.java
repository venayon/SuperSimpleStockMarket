package com.jp.ib.dao.impl;

import com.jp.ib.dao.StockDao;
import com.jp.ib.entity.Stock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class StockDaoImplTest {

    private StockDao stockDao;

    @Before
    public void setUp()  {
        stockDao = new StockDaoImpl();
    }

    @Test
    public void testGetStock_with_invalid_stocksymbol_failure()  {
        Optional<Stock> stock = stockDao.getStock(null);
        assertFalse(stock.isPresent());
    }

    @Test
    public void testGetStock_with_nonExist_stocksymbol_failure()  {
        Optional<Stock> stock = stockDao.getStock(null);
        assertFalse(stock.isPresent());
    }

    @Test
    public void testGetStock_with_stocksymbol_success()  {
        Optional<Stock> stock = stockDao.getStock("joe");
        assertThat("Returned Stock symbol must be ", "JOE", containsString(stock.get().getStockSymbol()));
    }

    @Test
    public void testGetAll_loaded_stocks_Success() {
        List<Stock> stockList = stockDao.getAll();
        assertThat("getAll must return stocks", stockList != null && stockList.size() > 0);
        assertThat("getAll must return stocks with size 5 ", stockList.size()==5);
    }
}