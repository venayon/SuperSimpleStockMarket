package com.jp.ib;

import com.jp.ib.dao.StockDao;
import com.jp.ib.dao.TradeDao;
import com.jp.ib.dao.impl.StockDaoImpl;
import com.jp.ib.dao.impl.TradeDaoImpl;
import com.jp.ib.entity.Trade;
import com.jp.ib.execption.StockServiceException;
import com.jp.ib.service.GlobalBeverageCorporationExchange;
import com.jp.ib.service.StockService;
import com.jp.ib.service.TradeService;
import com.jp.ib.service.impl.GlobalBeverageCorporationExchangeImpl;
import com.jp.ib.service.impl.StockServiceImpl;
import com.jp.ib.service.impl.TradeServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.*;

public class SuperSimpleStockMarketTest {
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    private static SuperSimpleStockMarket superSimpleStockMarket;
    private static StockDao stockDao;
    private static TradeDao tradeDao;
    private static StockService stockService;
    private static TradeService tradeService;
    private static GlobalBeverageCorporationExchange exchange;

    @BeforeClass
    public static void setupEnv() {
        stockDao = new StockDaoImpl();
        tradeDao = new TradeDaoImpl();
     //   MockTrades.recordTrads(System.currentTimeMillis());
    }

    @Before
    public void setUp(){
        stockService = new StockServiceImpl(stockDao);
        tradeService = new TradeServiceImpl(tradeDao);
        superSimpleStockMarket = new SuperSimpleStockMarket(stockService, tradeService);
        exchange = new GlobalBeverageCorporationExchangeImpl(stockService,tradeService);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void testCalculateAllShareIndex() throws StockServiceException {
    List<Trade> tradeList =  MockTrades.recordTrads(System.currentTimeMillis());
        for (Trade trade : tradeList){
            superSimpleStockMarket.recordTrade(trade.getStockSymbol(),trade.getIndicator().toString(),trade.getQuantity(),trade.getPrice(),trade.getTimeStamp());
        }
        BigDecimal calculateAllShareIndex = exchange.calculateAllShareIndex();
        assertThat("DividendYield must be",calculateAllShareIndex , closeTo(BigDecimal.valueOf(0.200), ZERO));

    }

    @Test
    public void test_calculateDividendYield_with_valid_arguments_success() throws Exception {
        String stockSymbol = "JOE";
        BigDecimal price = BigDecimal.valueOf(4);
        BigDecimal dividendYield = superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
        assertNotNull("calculateDividendYield must return a valid value ", dividendYield);
        assertThat("DividendYield must be",dividendYield , closeTo(BigDecimal.valueOf(0.033), ZERO));
    }

    @Test
    public void test_calculateDividendYield_with_valid_stock_symbol_price_success() throws Exception {
        String stockSymbol = "JOE";
        BigDecimal price = new BigDecimal("2");
        BigDecimal dividendYield = superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
        assertTrue("Must return a valid dividendYield", dividendYield != null && dividendYield.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void test_calculateDividendYield_for_common_stockType_success() throws Exception {
        String stockSymbol = "POP";
        BigDecimal price = new BigDecimal("2");
        BigDecimal dividendYield = superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
        assertThat("Common stockType dividendYield not correctly computed ", dividendYield, closeTo(BigDecimal.valueOf(0.040), ZERO));
    }

    @Test
    public void test_calculateDividendYield_for_Preferred_stockType_success() throws Exception {
        String stockSymbol = "GIN";
        BigDecimal price = new BigDecimal("2");
        BigDecimal dividendYield = superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
        assertThat("Preferred stockType dividendYield not correctly computed ", dividendYield, closeTo(BigDecimal.valueOf(0.010), ZERO));
    }

    @Test
    public void test_calculateDividendYield_with_invalid_stocksymbol_faliure() throws Exception {
        String stockSymbol = null;
        BigDecimal price = BigDecimal.ZERO;
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(startsWith("The stockSymbol must not be null"));
        superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
    }

    @Test
    public void test_calculateDividendYield_with_invalid_stockprice_faliure() throws Exception {
        String stockSymbol = "TEA";
        BigDecimal price = null;
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(startsWith("The stockprice must not be null"));
        superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
    }

    @Test
    public void test_calculateDividendYield_with_invalid_stockprice_zero_faliure() throws Exception {
        String stockSymbol = "TEA";
        BigDecimal price = BigDecimal.ZERO;
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(startsWith("Stock Price must be Greater then Zero"));
        superSimpleStockMarket.calculateDividendYield(stockSymbol, price);
    }
}