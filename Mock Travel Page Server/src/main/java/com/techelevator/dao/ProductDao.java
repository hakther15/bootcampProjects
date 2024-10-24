package com.techelevator.dao;

import com.techelevator.model.Product;

import java.util.List;

public interface ProductDao {

    Product getProductById (int productId);
    Product getProductBySku(String productSku);
    List<Product> getAllProducts();
    Product createProduct(Product newProduct);
}
