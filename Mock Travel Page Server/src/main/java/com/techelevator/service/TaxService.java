package com.techelevator.service;

import com.techelevator.model.TaxServiceDto;

//Interface used for RestTaxService
public interface TaxService {

    TaxServiceDto getTaxRate(String stateCode);
}
