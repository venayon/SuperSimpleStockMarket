package com.jp.ib.dao;

import com.jp.ib.entity.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDao {
    Optional<Stock> getStock(String stockSymbol);
    List<Stock> getAll();

}
