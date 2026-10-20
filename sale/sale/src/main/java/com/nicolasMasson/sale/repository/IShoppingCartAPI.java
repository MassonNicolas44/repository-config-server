package com.nicolasMasson.sale.repository;

import com.nicolasMasson.sale.dto.ShoppingCartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//Feing ShoppingCart
@FeignClient(name="shoppingCart-service")
public interface IShoppingCartAPI {
    @GetMapping("/shoppingCart/findShoppingCartSale")
    public ShoppingCartDTO getShoppingCart(@RequestParam("id") Long id);
}

