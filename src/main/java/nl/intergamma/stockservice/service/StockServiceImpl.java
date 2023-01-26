package nl.intergamma.stockservice.service;

import nl.intergamma.stockservice.domain.Claim;
import nl.intergamma.stockservice.domain.Stock;
import nl.intergamma.stockservice.domain.StockInfo;
import nl.intergamma.stockservice.exception.ArticleUnknownException;
import nl.intergamma.stockservice.repository.ClaimRepository;
import nl.intergamma.stockservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService{

    private final StockRepository stockRepository;
    private final ClaimRepository claimRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, ClaimRepository claimRepository){
        this.stockRepository = stockRepository;
        this.claimRepository = claimRepository;
    }

    @Override
    public StockInfo getStockInfo(String productCode, String location) throws ArticleUnknownException {
        List<Claim> claims = claimRepository.findByProductCodeAndLocation(productCode, location);
        Integer claimedAmount = claims.stream().map(Claim::getAmount).reduce(0,Integer::sum);

        return stockRepository
                .findByProductCodeAndLocation(productCode, location)
                .map(stock -> new StockInfo(productCode, stock.getAmountAvailable() - claimedAmount, location))
                .orElseThrow(ArticleUnknownException::new);
    }

    @Override
    public StockInfo saveStockInfo(StockInfo stockInfo) {
        Stock stock = Stock.builder()
                .productCode(stockInfo.productCode())
                .amountAvailable(stockInfo.amountAvailable())
                .location(stockInfo.location())
                .build();
        Stock savedStock = stockRepository.save(stock);
        return new StockInfo(savedStock.getProductCode(), savedStock.getAmountAvailable(), savedStock.getLocation());
    }

    @Override
    public StockInfo updateStockInfo(StockInfo stockInfo) throws ArticleUnknownException {
        Stock stock = stockRepository.findByProductCodeAndLocation(stockInfo.productCode(), stockInfo.location()).orElseThrow(ArticleUnknownException::new);
        stock.setLocation(stockInfo.location());
        stock.setAmountAvailable(stockInfo.amountAvailable());
        Stock savedStock = stockRepository.save(stock);

        return new StockInfo(savedStock.getProductCode(), savedStock.getAmountAvailable(), savedStock.getLocation());
    }

    @Override
    public void removeStockInfo(String productCode, String location) {
        stockRepository.deleteByProductCodeAndLocation(productCode, location);
    }
}
