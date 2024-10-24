package com.techelevator.model;

import java.math.BigDecimal;

/**
 * TaxService DTO is a class used to hold the sales tax information from the API used in RestTaxService

 * The acronym DTO is being used for "data transfer object". It means that this type of
 * class is specifically created to transfer data between the client and the server.
 */

public class TaxServiceDto {

    private BigDecimal salesTax;

    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }
}
