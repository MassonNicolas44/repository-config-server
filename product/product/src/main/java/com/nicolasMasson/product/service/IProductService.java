package com.nicolasMasson.product.service;

import com.nicolasMasson.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    Product saveProduct(Product product);
    Optional<Product> updateProduct(Long id, Product product);

    String deleteProduct(Long id);

    List<Product> findAllProduct();

    Optional<Product> findProductById(Long id);

    //Feing ShoppingCart
    List<Product> findProductsByIds(List<Long> ids);


}
