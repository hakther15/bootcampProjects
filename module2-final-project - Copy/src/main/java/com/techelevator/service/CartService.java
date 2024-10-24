package com.techelevator.service;

import com.techelevator.dao.CartDao;
import com.techelevator.dao.ProductDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cart;
import com.techelevator.model.CartTotalsDto;
import com.techelevator.model.TaxServiceDto;
import com.techelevator.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Component
@Service
public class CartService {

    private CartDao cartDao;
    private ProductDao productDao;
    private RestTaxService restTaxService;
    private UserDao userDao;
    public CartService(CartDao cartDao, ProductDao productDao, RestTaxService restTaxService, UserDao userDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
        this.restTaxService = restTaxService;
        this.userDao = userDao;
    }



    //OBTAIN SUBTOTAL FROM CART
    public BigDecimal getSubtotal (List<Cart> cartList) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Cart cart : cartList) {
            if (cart.getPrice() != null) {
                subtotal = subtotal.add(cart.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            }
        }
        return subtotal;
    }


    //OBTAIN TAX BASED ON STATE
    public TaxServiceDto calculateTax(String state) {
        try {
            TaxServiceDto stateTax = restTaxService.getTaxRate(state);
            if (stateTax == null || stateTax.getSalesTax() == null) {
                throw new DaoException("No tax rate data found for " + state);
            }
            return stateTax;
        } catch (DaoException e) {
            throw new DaoException("Error calculating tax for " + state, e);
        }
    }


    //OBTAIN TOTAL AFTER SUBTOTAL AND STATE SALES TAX
    public BigDecimal totalWithTax(BigDecimal subtotal, TaxServiceDto taxServiceDto) {
        if (subtotal == null) {
            throw new IllegalArgumentException("Subtotal cannot be null");
        }
        if (taxServiceDto == null || taxServiceDto.getSalesTax() == null) {
            throw new IllegalArgumentException("Tax rate cannot be null");
        }
        try {
            BigDecimal taxRate = taxServiceDto.getSalesTax().divide(new BigDecimal("100"));
            BigDecimal subtotalWithTax = subtotal.multiply(taxRate);
            return subtotal.add(subtotalWithTax).setScale(2, 4);
        } catch (ArithmeticException e) {
            throw new DaoException("Error calculating total with tax", e);
        }
    }



    //DISPLAYING TOTALS IN CART
    public CartTotalsDto calculateCartTotals(String userName, String state) {

        User user = userDao.getUserByUsername(userName);

        // Get cart items and calculate subtotal
        List<Cart> cartList = cartDao.viewCart(user.getId());
        BigDecimal subtotal = getSubtotal(cartList);


        // Calculate tax rate and grand total
        TaxServiceDto taxServiceDto = calculateTax(state);
        BigDecimal taxRate = taxServiceDto.getSalesTax().divide(new BigDecimal(100));
        BigDecimal grandTotal = totalWithTax(subtotal, taxServiceDto);


        // Display tax amount for users
        BigDecimal taxRateDisplay = subtotal.multiply(taxRate).setScale(2, 4);


        //String displays
        String subtotalAmount = "$" + subtotal.toString();
        String taxRateAmount = "$" + taxRateDisplay.toString();
        String grandTotalAmount = "$" + grandTotal.toString();

        return new CartTotalsDto(cartList, subtotalAmount, taxRateAmount, grandTotalAmount);
    }
}
