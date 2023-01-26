package nl.intergamma.stockservice.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClaimRequest(@NotBlank(message = "ProductCode can not be empty") String productCode, @Min(1) Integer amount, @NotBlank(message = "Can't claim stock without a location") String location){}
