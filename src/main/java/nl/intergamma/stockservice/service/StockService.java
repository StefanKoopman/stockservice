package nl.intergamma.stockservice.service;

import nl.intergamma.stockservice.domain.StockInfo;
import nl.intergamma.stockservice.exception.ArticleUnknownException;

public interface StockService {

    StockInfo getStockInfo(String productCode, String location) throws ArticleUnknownException;
    StockInfo saveStockInfo(StockInfo stockInfo);
    StockInfo updateStockInfo(StockInfo stockInfo) throws ArticleUnknownException;
    void removeStockInfo(String productCode, String location);
}
