package com.nicolasMasson.product.service;

import com.nicolasMasson.product.model.Product;
import com.nicolasMasson.product.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepo;

    @Override
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product) {

        Optional<Product> productOptional=productRepo.findById(id);

        if(productOptional.isPresent()){

            Product productNew=productOptional.get();

            productNew.setName(product.getName());
            productNew.setBrand(product.getBrand());
            productNew.setUnitPrice(product.getUnitPrice());

            return Optional.of(productRepo.save(productNew));
        }
        return Optional.empty();
    }

    @Override
    public String deleteProduct(Long id) {
        if(productRepo.findById(id).isPresent()){
            productRepo.deleteById(id);
            return "Product deleted";
        }
        return "Product not found";
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepo.findAll();
    }

    @Override
    public Optional<Product> findProductById(Long id) {return productRepo.findById(id);}

    //Feing ShoppingCart
    @Override
    public List<Product> findProductsByIds(List<Long> ids) {
        return productRepo.findAllById(ids);
    }
}
