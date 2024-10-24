package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Cart;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCartDao implements CartDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcCartDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Cart addItemToCart(Cart addItem) {
        try {
            String checkItemSql = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
            SqlRowSet result = jdbcTemplate.queryForRowSet(checkItemSql, addItem.getUserId(), addItem.getProductId());

            if (result.next()) {
                int currentQuantity = result.getInt("quantity");
                String updateQuantitySql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
                jdbcTemplate.update(updateQuantitySql, currentQuantity + addItem.getQuantity(), addItem.getUserId(), addItem.getProductId());
            } else {
                String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) RETURNING cart_id;";
                int addToCart = jdbcTemplate.queryForObject(sql, int.class, addItem.getUserId(), addItem.getProductId(), addItem.getQuantity());
            }
            String cartDetailsSql = "SELECT u.user_id, u.username, p.product_id, p.name AS product_name, p.product_sku, " +
                    "p.price, c.quantity, p.image_name FROM cart c " +
                    "JOIN product p ON c.product_id = p.product_id " +
                    "JOIN users u ON c.user_id = u.user_id WHERE c.user_id = ? AND c.product_id = ?;";
            SqlRowSet resultDetails = jdbcTemplate.queryForRowSet(cartDetailsSql, addItem.getUserId(), addItem.getProductId());
            if (resultDetails.next()) {
                return mapRowToCart(resultDetails);
            } else {
                throw new DaoException("Error retrieving cart details");
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Cart> viewCart(int userId) {
        List<Cart> carts = new ArrayList<>();
        try {
            String sql = "SELECT u.user_id, u.username, p.product_id, p.name AS product_name, " +
                    "p.product_sku, p.price, c.quantity, p.image_name " +
                    "FROM cart c " +
                    "JOIN product p ON c.product_id = p.product_id " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE u.user_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                carts.add(mapRowToCart(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
        return carts;
    }

    @Override
    public int updateQuantity(int productId, int quantity) {
        try {
                String sql = "UPDATE cart SET quantity = ? WHERE product_id = ?;";
                return jdbcTemplate.update(sql, quantity, productId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
    }

    @Override
        public int deleteItemFromCart(String username, int productId) {
        try {
            // Get the current quantity for the given product and user
            String selectSql = "SELECT c.quantity FROM cart c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE u.username = ? AND c.product_id = ?";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectSql, username, productId);

            if (rowSet.next()) {
                int currentQuantity = rowSet.getInt("quantity");

                if (currentQuantity > 1) {
                    // If quantity is greater than 1, decrement it
                    String updateSql = "UPDATE cart SET quantity = quantity - 1 " +
                            "WHERE user_id = (SELECT user_id FROM users WHERE username = ?) " +
                            "AND product_id = ?";
                    return jdbcTemplate.update(updateSql, username, productId);
                } else {
                    // If quantity is 1, delete the item from the cart
                    String deleteSql = "DELETE FROM cart WHERE user_id = (SELECT user_id FROM users WHERE username = ?) " +
                            "AND product_id = ?";
                    return jdbcTemplate.update(deleteSql, username, productId);
                }
            } else {
                throw new DaoException("Item not found in the cart.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int clearCartByUsername(String username) {
        try {
            String sql = "DELETE FROM cart USING users WHERE cart.user_id = users.user_id AND users.username = ?;";
            return jdbcTemplate.update(sql, username);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
    }

    @Override
    public Cart findCartByProductId(int productId) {
        try {
            String sql = "SELECT u.user_id, u.username, p.product_id, p.name AS product_name, p.product_sku, " +
                    "p.price, c.quantity FROM cart c " +
                    "JOIN product p ON c.product_id = p.product_id " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.product_id = ?";
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, productId);
            if (result.next()) {
                return mapRowToCart(result);
            } else {
                return null;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation.", e);
        }
    }

    private Cart mapRowToCart (SqlRowSet results) {
        Cart cart = new Cart();
        cart.setUserId(results.getInt("user_id"));
        cart.setProductId(results.getInt("product_id"));
        cart.setProductName(results.getString("product_name"));
        cart.setProductSku(results.getString("product_sku"));
        cart.setPrice(results.getBigDecimal("price"));
        cart.setQuantity(results.getInt("quantity"));
        cart.setUsername(results.getString("username"));
        cart.setImageName(results.getString("image_name"));
        return cart;
    }
}
