package com.techelevator.controller;

import com.techelevator.dao.ProductDao;

import com.techelevator.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    private final ProductDao productDao;
    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    //VIEW PRODUCTS
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Product> list() {
        return productDao.getAllProducts();
    }

    //VIEW PRODUCTS BY ID
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/{productId}", method = RequestMethod.GET)
    public Product get(@PathVariable int productId) {
        Product product = productDao.getProductById(productId);
        if(product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        } else {
            return product;
        }
    }

    //VIEW PRODUCTS BY SKU
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/sku/{productSku}", method = RequestMethod.GET)
    public Product get(@PathVariable String productSku) {
        Product product = productDao.getProductBySku(productSku);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        } else {
            return product;
        }
    }

    //ADD NEW ITEM TO PRODUCT LIST AS AN ADMIN
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public Product add(@Valid @RequestBody Product product) {
        Product newProduct = productDao.createProduct(product);
        return newProduct;
    }
}
