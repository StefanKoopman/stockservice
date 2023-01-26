package nl.intergamma.stockservice.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StockInfo(@NotBlank(message = "ProductCode can not be empty") String productCode, @Min(0) Integer amountAvailable, @NotBlank(message = "Location can not be empty") String location){}
