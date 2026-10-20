package com.nicolasMasson.shoppingCart.service;

import com.nicolasMasson.shoppingCart.dto.ProductCart;
import com.nicolasMasson.shoppingCart.dto.ProductDTO;
import com.nicolasMasson.shoppingCart.model.ShoppingCart;
import com.nicolasMasson.shoppingCart.repository.IProductAPI;
import com.nicolasMasson.shoppingCart.repository.IShoppingCartRepository;
import com.nicolasMasson.shoppingCart.service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    private IShoppingCartRepository shoppingCartRepo;

    @Autowired
    private IProductAPI productAPI;

    @Override
    @CircuitBreaker(name="product-service",fallbackMethod = "fallbackSaveShoppingCart")
    @Retry(name="product-service")
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {

        //Ids for the products
        List<Long> ids = shoppingCart.getListProduct().stream().map(ProductDTO::getId).toList();

        //Find product with Feing
        List<ProductDTO> products = productAPI.getProducts(ids);

        //Quantity for ShoppingCart
        Map<Long, Integer> quantities = shoppingCart.getListProduct().stream().collect(Collectors.toMap(ProductDTO::getId,
                ProductDTO::getQuantity));

        //Setter the quantity to product
        products.forEach(product ->product.setQuantity(quantities.get(product.getId())));

        //Calculate total price
        Double total = products.stream().mapToDouble(product ->product.getUnitPrice() * product.getQuantity()).sum();

        //Save ids products and quantity
        List<ProductCart> productsCart = shoppingCart.getListProduct()
                .stream()
                .map(product -> {
                    ProductCart pc = new ProductCart();
                    pc.setProductId(product.getId());
                    pc.setQuantity(product.getQuantity());
                    return pc;
                })
                .toList();

        ShoppingCart shoppingCartNew = new ShoppingCart();

        shoppingCartNew.setListProduct(products);
        shoppingCartNew.setProductsCart(productsCart);
        shoppingCartNew.setTotalPrice(total);

        return shoppingCartRepo.save(shoppingCartNew);
    }

    @Override
    @CircuitBreaker(name="product-service",fallbackMethod = "fallbackUpdateShoppingCart")
    @Retry(name="product-service")
    public Optional<ShoppingCart> updateShoppingCart(Long id, ShoppingCart shoppingCart) {


        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepo.findById(id);

        if(shoppingCartOptional.isPresent()) {
            ShoppingCart shoppingCartNew = shoppingCartOptional.get();

            //Ids for the products
            List<Long> ids = shoppingCart.getListProduct().stream().map(ProductDTO::getId).toList();

            //Find product with Feing
            List<ProductDTO> products = productAPI.getProducts(ids);

            //Quantity for ShoppingCart
            Map<Long, Integer> quantities = shoppingCart.getListProduct().stream().collect(Collectors.toMap(ProductDTO::getId,
                    ProductDTO::getQuantity));

            //Setter the quantity to product
            products.forEach(product ->product.setQuantity(quantities.get(product.getId())));

            //Calculate total price
            Double total = products.stream().mapToDouble(product ->product.getUnitPrice() * product.getQuantity()).sum();

            //Save ids products and quantity
            List<ProductCart> productsCart = shoppingCart.getListProduct()
                    .stream()
                    .map(product -> {
                        ProductCart pc = new ProductCart();
                        pc.setProductId(product.getId());
                        pc.setQuantity(product.getQuantity());
                        return pc;
                    })
                    .collect(Collectors.toList());

            shoppingCartNew.setListProduct(products);
            shoppingCartNew.setProductsCart(productsCart);
            shoppingCartNew.setTotalPrice(total);

            return Optional.of(shoppingCartRepo.save(shoppingCartNew));
        }

        return Optional.empty();
    }

    @Override
    public String deleteShoppingCart(Long id) {

        if(shoppingCartRepo.findById(id).isPresent()){
            shoppingCartRepo.deleteById(id);
            return "Shopping Cart deleted";
        }
        return "Shopping Cart not found";
    }

    @Override
    @CircuitBreaker(name="product-service",fallbackMethod = "fallbackFindById")
    @Retry(name="product-service")
    public Optional<ShoppingCart> findById(Long id) {

        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepo.findById(id);

        if (shoppingCartOptional.isPresent()) {

            ShoppingCart cart = shoppingCartOptional.get();

            List<Long> ids = cart.getProductsCart()
                    .stream()
                    .map(ProductCart::getProductId)
                    .toList();

            List<ProductDTO> products =productAPI.getProducts(ids);

            Map<Long, Integer> quantities = cart.getProductsCart()
                    .stream()
                    .collect(Collectors.toMap(
                            ProductCart::getProductId,
                            ProductCart::getQuantity
                    ));

            products.forEach(product ->
                    product.setQuantity(quantities.get(product.getId()))
            );

            cart.setListProduct(products);

            return Optional.of(cart);
        }

        return Optional.empty();
    }

    @Override
    @CircuitBreaker(name="product-service",fallbackMethod = "fallbackFindAllShoppingCart")
    @Retry(name="product-service")
    public List<ShoppingCart> findAllShoppingCart() {

        List<ShoppingCart> shoppingCarts = shoppingCartRepo.findAll();

        shoppingCarts.forEach(cart -> {

            List<Long> ids = cart.getProductsCart()
                    .stream()
                    .map(ProductCart::getProductId)
                    .toList();

            List<ProductDTO> products =productAPI.getProducts(ids);

            Map<Long, Integer> quantities = cart.getProductsCart()
                    .stream()
                    .collect(Collectors.toMap(
                            ProductCart::getProductId,
                            ProductCart::getQuantity
                    ));

            products.forEach(product ->
                    product.setQuantity(quantities.get(product.getId()))
            );

            cart.setListProduct(products);
        });

        return shoppingCarts;

    }

    @Override
    public Optional<ShoppingCart> findShoppingCartSale(Long id) {
        return shoppingCartRepo.findById(id);
    }



    //Circuit Breaker for Save ShoppingCart
    public ShoppingCart fallbackSaveShoppingCart(Throwable throwable) {
        throw new ServiceUnavailableException("Not save ShoppingCart. Try again.");
    }

    //Circuit Breaker for Update ShoppingCart
    public Optional<ShoppingCart> fallbackUpdateShoppingCart(Throwable throwable) {
        throw new ServiceUnavailableException("Not update ShoppingCart. Try again.");
    }

    //Circuit Breaker for Find One ShoppingCart
    public Optional<ShoppingCart> fallbackFindById(Throwable throwable) {
        throw new ServiceUnavailableException("Not find ShoppingCart. Try again.");
    }

    //Circuit Breaker for Find All ShoppingCart
    public List<ShoppingCart> fallbackFindAllShoppingCart(Throwable throwable) {
        throw new ServiceUnavailableException("Not find all ShoppingCart. Try again.");
    }

}