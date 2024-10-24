package com.techelevator.controller;

import com.techelevator.dao.CartDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cart;
import com.techelevator.model.CartTotalsDto;
import com.techelevator.security.SecurityUtils;
import com.techelevator.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.techelevator.dao.UserDao;

import java.security.Principal;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    private final CartDao cartDao;
    private final CartService cartService;
    private final UserDao userDao;

    public CartController(CartDao cartDao, CartService cartService, UserDao userDao) {
        this.cartDao = cartDao;
        this.cartService = cartService;
        this.userDao = userDao;
    }

    //ADD ITEM TO CART
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public Cart add(@Valid @RequestBody Cart cart) {
        String currentUsername = SecurityUtils.getCurrentUsername().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
        int userId = userDao.getUserIdFromUsername(currentUsername);
        cart.setUserId(userId);
        try {
            return cartDao.addItemToCart(cart);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cart item cannot be added.");
        }

    }



    //UPDATE QUANTITY IN CART
    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    public void updateQuantity(@RequestBody Map<String, Integer> body) {
        Integer productId = (Integer) body.get("productId");
        Integer quantity = body.get("quantity");
        String currentUsername = SecurityUtils.getCurrentUsername().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        Cart cart = cartDao.findCartByProductId(productId);
        if(cart == null || !currentUsername.equals(cart.getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot modify this cart");
        }

        if (quantity == null || quantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity.");
        }

        int result = cartDao.updateQuantity(productId, quantity);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item cannot be found or updated.");
        }
    }

    //DELETE AN ITEM FROM CART
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/delete/{productId}")
    public void delete(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            cartDao.deleteItemFromCart(username, productId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot remove.");
        }
    }

    //CLEAR CART USING USERNAME
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/clear")
    public void delete(Principal user) {
        String username = user.getName();
        if (cartDao.clearCartByUsername(username) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for username");
        }
    }


    //VIEW CART ITEMS AND TOTALS
    @RequestMapping(path = "/total", method = RequestMethod.GET)
    public CartTotalsDto getCartTotals(@RequestParam String stateCode, Principal user) {
        return cartService.calculateCartTotals(user.getName(), stateCode);
    }
}
