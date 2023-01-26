package nl.intergamma.stockservice.service;

import nl.intergamma.stockservice.domain.ClaimRequest;

public interface ClaimService {
    void claimStock(ClaimRequest claim);
    void removeExpiredClaims();
}
