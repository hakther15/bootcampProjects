package com.techelevator.model;

import java.util.List;

/**
 * CartTotals DTO is a class used to hold and display the totals used in the CartService

 * The acronym DTO is being used for "data transfer object". It means that this type of
 * class is specifically created to transfer data between the client and the server.
 */

public class CartTotalsDto {

    private List<Cart> products;
    private String subtotal;
    private String tax;
    private String grandTotal;

    public List<Cart> getProducts() {
        return products;
    }

    public void setProducts(List<Cart> products) {
        this.products = products;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public CartTotalsDto (List<Cart> products, String subtotal, String tax, String grandTotal) {
        this.products = products;
        this.subtotal = subtotal;
        this.tax = tax;
        this.grandTotal = grandTotal;
    }

}
