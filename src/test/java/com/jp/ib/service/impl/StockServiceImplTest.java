package com.jp.ib.service.impl;

import com.jp.ib.dao.StockDao;
import com.jp.ib.entity.Stock;
import com.jp.ib.entity.util.StockType;
import com.jp.ib.execption.StockServiceException;
import com.jp.ib.service.StockService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockServiceImplTest {

    private StockService stockService;

    private StockDao stockDao = mock(StockDao.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        stockService = new StockServiceImpl(stockDao);
    }

    @Test
    public void testComputePERatio_for_common_stock_success() throws Exception {
        String stockSymbol = "GIN";
        BigDecimal stockPrice = new BigDecimal("0.1");
        Stock stock1 = new Stock(stockSymbol, StockType.COMMON, BigDecimal.valueOf(0.08f), BigDecimal.valueOf(0.02f), BigDecimal.ONE);

        when(stockDao.getStock(stockSymbol)).thenReturn(Optional.of(stock1));

        BigDecimal actual_priceEarningsRatio = stockService.calculatePriceEarningsRatio(stockSymbol, stockPrice);

        assertThat("calculated Price earning ratio incorrect", BigDecimal.valueOf(1.25),  comparesEqualTo(actual_priceEarningsRatio));
    }

    @Test
    public void testComputePERatio_for_preferred_stock_success() throws Exception {
        String stockSymbol = "GIN";
        BigDecimal stockPrice = new BigDecimal("0.1");
        Stock stock = new Stock(stockSymbol, StockType.PREFERRED, BigDecimal.valueOf(0.08f), BigDecimal.valueOf(0.02f), BigDecimal.ONE);
        when(stockDao.getStock(stockSymbol)).thenReturn(Optional.of(stock));

        BigDecimal actual_priceEarningsRatio = stockService.calculatePriceEarningsRatio(stockSymbol, stockPrice);

        assertThat("calculated Price earning ratio incorrect",BigDecimal.valueOf(5),  comparesEqualTo(actual_priceEarningsRatio));
    }

    @Test
    public void testCalculateDividendYield_common_stock_success() throws Exception {
        String stockSymbol = "GIN";
        BigDecimal stockPrice = BigDecimal.valueOf(2);
        Stock stock = new Stock(stockSymbol, StockType.COMMON, new BigDecimal("0.08"), null, null);
        when(stockDao.getStock(stockSymbol)).thenReturn(Optional.of(stock));

        BigDecimal actual_dividendYield = stockService.calculateDividendYield(stockSymbol, stockPrice);

        assertThat("calculated common dividend yield is incorrect ", BigDecimal.valueOf(0.04),  comparesEqualTo(actual_dividendYield));
    }

    @Test
    public void testCalculateDividendYield_Preferred_stock_success() throws Exception {
        String stockSymbol = "GIN";
        BigDecimal stockPrice = BigDecimal.valueOf(2);
        Stock stock = new Stock(stockSymbol, StockType.PREFERRED, new BigDecimal("0.08"), new BigDecimal("0.02"), new BigDecimal("1.0"));
        when(stockDao.getStock(stockSymbol)).thenReturn(Optional.of(stock));

        BigDecimal actual_dividendYield = stockService.calculateDividendYield(stockSymbol, stockPrice);

        assertThat("calculated preferred dividend yield is incorrect ", BigDecimal.valueOf(0.01),  comparesEqualTo(actual_dividendYield));
    }

    @Test
    public void testGetStock_with_invalid_stockType_StockPrice_throws_unchecked_exception_Failure() throws Exception {
        String stockSymbol = "JOE";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Invalid Stock type received ");
        Stock stock = new Stock(stockSymbol, null, new BigDecimal("0.08"), new BigDecimal("0.02"), new BigDecimal("1.0"));
        when(stockDao.getStock(stockSymbol)).thenReturn(Optional.of(stock));
        stockService.calculateDividendYield(stockSymbol, BigDecimal.ONE);
    }

    @Test
    public void testGetStock_with_Zero_StockPrice_throws_IllegalArgumentException_Failure() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Stock Price must be Greater then Zero");
        stockService.calculateDividendYield("JOE", BigDecimal.ZERO);
    }

    @Test
    public void testGetStock_with_inValid_StockSymbol_throws_NoDataFoungException_Failure() throws Exception {
        expectedException.expect(StockServiceException.class);
        expectedException.expectMessage("No stock present for symbol:");
        stockService.calculateDividendYield("RVU", BigDecimal.ONE);
    }

    @Test
    public void testGetStock_with_inValid_StockSymbol_throws_IllegalArgumentException_Failure() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The stockSymbol must not be null");
        stockService.calculateDividendYield(null, BigDecimal.ONE);
    }


    @Test
    public void testGetStock_with_inValid_StockPrice_throws_IllegalArgumentException_Failure() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The stockprice must not be null");
        stockService.calculateDividendYield("JOE", null);
    }

    @Test
    public void testGetStock_with_Negative_StockPrice_throws_IllegalArgumentException_Failure() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Stock Price must be Greater then Zero");
        stockService.calculateDividendYield("JOE", BigDecimal.ZERO);
    }

}