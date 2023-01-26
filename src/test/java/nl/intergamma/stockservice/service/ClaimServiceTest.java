package nl.intergamma.stockservice.service;

import nl.intergamma.stockservice.domain.Claim;
import nl.intergamma.stockservice.domain.ClaimRequest;
import nl.intergamma.stockservice.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class ClaimServiceTest {

    @Mock
    ClaimRepository claimRepository;

    ClaimService claimService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        claimService = new ClaimServiceImpl(claimRepository);
    }

    @Test
    public void shouldClaim(){
        ClaimRequest claimRequest = new ClaimRequest("12345", 3, "Leusden");

        claimService.claimStock(claimRequest);

        Mockito.verify(claimRepository).save(any());
    }

    @Test
    public void shouldRemoveOldClaims(){
        List<Claim> expiredClaims = Arrays.asList(Claim.builder().id(1L).build(), Claim.builder().id(2L).build());

        Mockito.when(claimRepository.findByClaimedAtAfter(any())).thenReturn(expiredClaims);

        claimService.removeExpiredClaims();

        Mockito.verify(claimRepository).deleteAll(expiredClaims);
    }
}
