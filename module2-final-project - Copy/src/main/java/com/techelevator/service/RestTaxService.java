package com.techelevator.service;

import com.techelevator.exception.DaoException;
import com.techelevator.model.TaxServiceDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//RestTaxService holds API used to calculate Sales Tax
@Component
public class RestTaxService implements TaxService{
    private final String BASE_API_URL = "https://teapi.netlify.app/api/statetax?state=";
    RestTemplate restTemplate = new RestTemplate();


    @Override
    public TaxServiceDto getTaxRate(String stateCode) {
        String endpoint = BASE_API_URL + stateCode.toUpperCase();
        try {
            TaxServiceDto taxServiceDto = restTemplate.getForObject(endpoint, TaxServiceDto.class);
            if (taxServiceDto == null || taxServiceDto.getSalesTax() == null) {
                throw new DaoException("Tax rate could not be retrieved from " + stateCode);
            }
            return taxServiceDto;
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
}
