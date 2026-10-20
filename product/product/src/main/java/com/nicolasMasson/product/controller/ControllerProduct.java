package com.nicolasMasson.product.controller;

import com.nicolasMasson.product.model.Product;
import com.nicolasMasson.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ControllerProduct {

    @Autowired
    private ProductService productService;

    @PostMapping("/save")
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/update/{id}")
    public Object updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> updateProduct = productService.updateProduct(id,product);
        if(updateProduct.isEmpty()) {
            return "Product not found";
        }

        return updateProduct.get();
    }

    @GetMapping("/findAll")
    public List<Product> findAll() {
        return productService.findAllProduct();
    }

    @GetMapping("/find/{id}")
    public Object findProduct(@PathVariable Long id) {
        Optional<Product> findProduct=productService.findProductById(id);

        if(findProduct.isEmpty()){
            return "Product not found";
        }

        return findProduct.get();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteProduct(@PathVariable Long id) {
        Optional<Product> findProduct=productService.findProductById(id);

        if(findProduct.isPresent()){
            return productService.deleteProduct(id);
        }
        return "Product no exist";
    }


    //Feing ShoppingCart
    @GetMapping("/findProducts")
    public List<Product> findProducts(@RequestParam List<Long> ids) {
        return productService.findProductsByIds(ids);
    }


}
