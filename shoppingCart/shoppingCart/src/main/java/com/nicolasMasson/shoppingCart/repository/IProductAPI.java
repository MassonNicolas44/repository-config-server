package com.nicolasMasson.shoppingCart.repository;

import com.nicolasMasson.shoppingCart.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//Feing Product
@FeignClient(name="product-service")
public interface IProductAPI {
    @GetMapping("/product/findProducts")
    public List<ProductDTO> getProducts(@RequestParam("ids") List<Long> ids);
}
