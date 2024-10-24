package com.techelevator.dao;


import com.techelevator.exception.DaoException;
import com.techelevator.model.Product;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcProductDao implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Product getProductById(int productId) {
        Product product = null;
        try {
            String sql = "SELECT product_id, product_sku, name, description, price, image_name " +
                    "FROM product WHERE product_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, productId);
            if(results.next()) {
                product = mapRowToProduct(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return product;
    }

    @Override
    public Product getProductBySku(String productSku) {
        Product product = null;
        try {
            String sql = "SELECT product_id, product_sku, name, description, price, image_name " +
                    "FROM product WHERE product_sku = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, productSku);
            if(results.next()) {
                product = mapRowToProduct(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try{
            String sql = "SELECT product_id, product_sku, name, description, price, image_name FROM product;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()) {
                products.add(mapRowToProduct(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return products;
    }

    @Override
    public Product createProduct(Product newProduct) {
        String sql = "INSERT INTO product " +
                "(product_sku, name, description, price, image_name) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING product_id;";
        try {
            int newProductId = jdbcTemplate.queryForObject(sql, int.class, newProduct.getProductSku(), newProduct.getName(), newProduct.getDescription(), newProduct.getPrice(), newProduct.getImageName());
            return getProductById(newProductId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
    }

    private Product mapRowToProduct (SqlRowSet results) {
        Product product = new Product();
        product.setProductId(results.getInt("product_id"));
        product.setProductSku(results.getString("product_sku"));
        product.setName(results.getString("name"));
        product.setDescription(results.getString("description"));
        product.setPrice(results.getBigDecimal("price"));
        product.setImageName(results.getString("image_name"));
        return product;
    }
}
