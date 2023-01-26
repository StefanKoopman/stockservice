package nl.intergamma.stockservice.repository;

import nl.intergamma.stockservice.domain.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, String> {
    Optional<Stock> findByProductCodeAndLocation(String productCode, String location);
    Long deleteByProductCodeAndLocation(String productCode, String location);
}
