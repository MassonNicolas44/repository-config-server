package com.nicolasMasson.shoppingCart.repository;

import com.nicolasMasson.shoppingCart.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
}
