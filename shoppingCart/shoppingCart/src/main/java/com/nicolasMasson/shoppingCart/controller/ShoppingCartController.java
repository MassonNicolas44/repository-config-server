package com.nicolasMasson.shoppingCart.controller;

import com.nicolasMasson.shoppingCart.model.ShoppingCart;
import com.nicolasMasson.shoppingCart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/save")
    public ShoppingCart saveShoppingCart(@RequestBody  ShoppingCart shoppingCart) {
        return shoppingCartService.saveShoppingCart(shoppingCart);
    }

    @PutMapping("/update/{id}")
    public Object updateShoppingCart(@PathVariable Long id,@RequestBody ShoppingCart shoppingCart) {
        Optional<ShoppingCart> shoppingCartUpdate=shoppingCartService.updateShoppingCart(id, shoppingCart);
        if (shoppingCartUpdate.isEmpty()) {
            return "Shopping Cart Not Found";
        }
        return shoppingCartUpdate.get();
    }

    @GetMapping("/findAll")
    public List<ShoppingCart> findAllShoppingCart() {
        return shoppingCartService.findAllShoppingCart();
    }

    @GetMapping("/find/{id}")
    public Object findShoppingCart(@PathVariable Long id) {
        Optional<ShoppingCart> shoppingCartFind=shoppingCartService.findById(id);
        if (shoppingCartFind.isEmpty()) {
            return "Shopping Cart Not Found";
        }
        return shoppingCartFind.get();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteShoppingCart(@PathVariable Long id) {
        Optional<ShoppingCart> shoppingCartDelete=shoppingCartService.findById(id);

        if(shoppingCartDelete.isEmpty()){
            return "Shopping Cart Not Found";
        }

        shoppingCartService.deleteShoppingCart(id);
        return "Shopping Cart delete";
    }

    //Feing Sale
    @GetMapping("/findShoppingCartSale")
    public Object findShoppingCartSale(@RequestParam Long id) {
        return shoppingCartService.findShoppingCartSale(id);
    }








}
