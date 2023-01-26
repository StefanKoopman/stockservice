package nl.intergamma.stockservice.service;

import nl.intergamma.stockservice.domain.Claim;
import nl.intergamma.stockservice.domain.Stock;
import nl.intergamma.stockservice.domain.StockInfo;
import nl.intergamma.stockservice.exception.ArticleUnknownException;
import nl.intergamma.stockservice.repository.ClaimRepository;
import nl.intergamma.stockservice.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

public class StockServiceTest {

    @Mock
    StockRepository stockRepository;

    @Mock
    ClaimRepository claimRepository;

    StockService stockService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        stockService = new StockServiceImpl(stockRepository, claimRepository);
    }

    @Test
    public void shouldThrowExceptionWhenArticleIsUnknown(){
        Mockito.when(stockRepository.findByProductCodeAndLocation("unknown", "Leusden")).thenReturn(Optional.empty());

        assertThrows(ArticleUnknownException.class, () -> stockService.getStockInfo("unknown", "Leusden"));
    }

    @Test
    public void shouldGetStockInfoWhenArticleIsKnown() throws ArticleUnknownException {
        Stock stock = Stock.builder()
                .location("Leusden")
                .productCode("12345")
                .amountAvailable(5)
                .build();

        Mockito.when(stockRepository.findByProductCodeAndLocation("12345", "Leusden"))
                .thenReturn(Optional.of(stock));

        StockInfo stockInfo = stockService.getStockInfo("12345", "Leusden");
        assertEquals(stockInfo.amountAvailable(), stock.getAmountAvailable());
        assertEquals(stockInfo.location(), stock.getLocation());
        assertEquals(stockInfo.productCode(), stock.getProductCode());
    }

    @Test
    public void shouldGetStockInfoWhenArticleHasClaims() throws ArticleUnknownException {
        Stock stock = Stock.builder()
                .location("Leusden")
                .productCode("12345")
                .amountAvailable(5)
                .build();

        Mockito.when(stockRepository.findByProductCodeAndLocation("12345", "Leusden"))
                .thenReturn(Optional.of(stock));

        Claim claim = Claim.builder()
                .amount(2)
                .location("Leusden")
                .productCode("12345")
                .claimedAt(LocalDateTime.now())
                .build();

        Mockito.when(claimRepository.findByProductCodeAndLocation("12345", "Leusden"))
                .thenReturn(Collections.singletonList(claim));

        StockInfo stockInfo = stockService.getStockInfo("12345", "Leusden");
        assertEquals(stockInfo.amountAvailable(), stock.getAmountAvailable() - claim.getAmount());
        assertEquals(stockInfo.location(), stock.getLocation());
        assertEquals(stockInfo.productCode(), stock.getProductCode());
    }

    @Test
    public void shouldSaveStockInfo(){
        Stock stock = Stock.builder().productCode("12345").amountAvailable(5).location("Leusden").build();
        StockInfo stockInfo = new StockInfo("12345", 5, "Leusden");

        Mockito.when(stockRepository.save(stock)).thenReturn(stock);

        stockService.saveStockInfo(stockInfo);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingUnknownArticle(){
        StockInfo stockInfo = new StockInfo("unknown", 5, "Leusden");

        Mockito.when(stockRepository.findByProductCodeAndLocation("unknown", "Leusden")).thenReturn(Optional.empty());

        assertThrows(ArticleUnknownException.class, () -> stockService.updateStockInfo(stockInfo));
    }

    @Test
    public void shouldUpdateExistingStockInfo() throws ArticleUnknownException {
        StockInfo stockInfo = new StockInfo("12345", 2, "Leusden");
        Stock stock = Stock.builder().id(1L).location("Leusden").productCode("12345").amountAvailable(5).build();

        Stock expectedStock = Stock.builder().id(1L).location("Leusden").productCode("12345").amountAvailable(2).build();

        Mockito.when(stockRepository.findByProductCodeAndLocation("12345", "Leusden")).thenReturn(Optional.of(stock));

        Mockito.when(stockRepository.save(expectedStock)).thenReturn(expectedStock);

        stockService.updateStockInfo(stockInfo);
    }

}
