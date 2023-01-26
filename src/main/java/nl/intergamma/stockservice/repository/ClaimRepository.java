package nl.intergamma.stockservice.repository;

import nl.intergamma.stockservice.domain.Claim;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClaimRepository extends CrudRepository<Claim, Long> {
    List<Claim> findByClaimedAtAfter(LocalDateTime oldestTimestamp);
    List<Claim> findByProductCodeAndLocation(String productCode, String location);
}
