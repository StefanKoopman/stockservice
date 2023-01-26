package nl.intergamma.stockservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.intergamma.stockservice.domain.ClaimRequest;
import nl.intergamma.stockservice.domain.StockInfo;
import nl.intergamma.stockservice.exception.ArticleUnknownException;
import nl.intergamma.stockservice.service.ClaimService;
import nl.intergamma.stockservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/stock")
@Tag(name = "Stock API")
public class StockController {

    private final StockService stockService;
    private final ClaimService claimService;

    @Autowired
    public StockController(StockService stockService, ClaimService claimService){
        this.stockService = stockService;
        this.claimService = claimService;
    }

    @GetMapping(path = "/{productCode}/{location}")
    @Operation(summary = "Get stock info for a specific productCode and (market) location")
    public StockInfo getStockInfo(@PathVariable(name = "productCode") String productCode, @PathVariable(name = "location") String location){
        try {
            return stockService.getStockInfo(productCode, location);
        } catch (ArticleUnknownException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add stock info for a specific productCode and (market) location")
    public StockInfo saveStockInfo(@Valid @RequestBody StockInfo stockInfo){
        return stockService.saveStockInfo(stockInfo);
    }

    @PostMapping(path = "/claim")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Reserve (some of) the available stock for 5 minutes")
    public void claimStock(@Valid @RequestBody ClaimRequest claimRequest){
         claimService.claimStock(claimRequest);
    }

    @PutMapping
    @Operation(summary = "Update stock info for a specific productCode and (market) location")
    public StockInfo updateStockInfo(@Valid @RequestBody StockInfo stockInfo){
        try {
            return stockService.updateStockInfo(stockInfo);
        } catch (ArticleUnknownException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{productCode}/{location}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove stock info for a specific productCode and (market) location")
    public void removeStockInfo(@PathVariable(name = "location") String location, @PathVariable(name = "productCode") String productCode){
        stockService.removeStockInfo(productCode, location);
    }

}
