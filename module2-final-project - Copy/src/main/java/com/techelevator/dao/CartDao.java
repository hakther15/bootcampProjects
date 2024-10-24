package com.techelevator.dao;

import com.techelevator.model.Cart;

import java.security.Principal;
import java.util.List;

public interface CartDao {

    Cart addItemToCart(Cart addItem);
    List<Cart> viewCart(int userId);
    int updateQuantity(int productId, int quantity);
    int deleteItemFromCart(String username, int productId);
    int clearCartByUsername(String username);

    Cart findCartByProductId(int productId);

}
