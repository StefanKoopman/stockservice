package nl.intergamma.stockservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Claim {

    @Id
    @GeneratedValue
    Long id;

    String productCode;

    Integer amount;

    @Column(name = "claimed_at")
    LocalDateTime claimedAt;

    String location;
}
