package nl.intergamma.stockservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue
    Long id;

    String productCode;

    @Column(name = "amount_available")
    Integer amountAvailable;

    String location;
}
