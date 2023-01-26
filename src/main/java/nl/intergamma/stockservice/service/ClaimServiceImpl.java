package nl.intergamma.stockservice.service;

import nl.intergamma.stockservice.domain.Claim;
import nl.intergamma.stockservice.domain.ClaimRequest;
import nl.intergamma.stockservice.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ClaimServiceImpl implements ClaimService{

    private final ClaimRepository claimRepository;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository){
        this.claimRepository = claimRepository;
    }

    @Override
    public void claimStock(ClaimRequest claimRequest) {
        Claim claim = Claim
                .builder()
                .productCode(claimRequest.productCode())
                .amount(claimRequest.amount())
                .location(claimRequest.location())
                .claimedAt(LocalDateTime.now())
                .build();

        claimRepository.save(claim);
    }

    @Override
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void removeExpiredClaims(){
        List<Claim> claims = claimRepository.findByClaimedAtAfter(LocalDateTime.now().minusMinutes(5));
        claimRepository.deleteAll(claims);
    }
}
