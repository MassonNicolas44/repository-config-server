package com.nicolasMasson.shoppingCart.service;

import com.nicolasMasson.shoppingCart.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface IShoppingCartService {

    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
    Optional<ShoppingCart> updateShoppingCart(Long id, ShoppingCart shoppingCart);

    String deleteShoppingCart(Long id);

    Optional<ShoppingCart> findById(Long id);

    List<ShoppingCart> findAllShoppingCart();

    //Feing Sale
    Optional<ShoppingCart> findShoppingCartSale(Long id);

}
