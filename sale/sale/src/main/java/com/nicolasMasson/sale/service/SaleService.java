package com.nicolasMasson.sale.service;

import com.nicolasMasson.sale.dto.SaleDTO;
import com.nicolasMasson.sale.dto.ShoppingCartDTO;
import com.nicolasMasson.sale.model.Sale;
import com.nicolasMasson.sale.repository.ISaleRepository;
import com.nicolasMasson.sale.repository.IShoppingCartAPI;
import com.nicolasMasson.sale.service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private ISaleRepository saleRepo;

    @Autowired
    private IShoppingCartAPI shoppingCartAPI;

    @Override
    @CircuitBreaker(name="shoppingCart-service", fallbackMethod="fallbackSaveSale")
    @Retry(name="shoppingCart-service")
    public Sale saveSale(Sale sale) {

        //Ids for the shopping cart
        Long id = sale.getIdShoppingCart();

        //Find shopping cart with Feing
        ShoppingCartDTO shoppingCarts = shoppingCartAPI.getShoppingCart(id);

        Sale saleNew=new Sale();

        saleNew.setDateSale(sale.getDateSale());
        saleNew.setIdShoppingCart(shoppingCarts.getId());

        return saleRepo.save(saleNew);

    }

    @Override
    @CircuitBreaker(name="shoppingCart-service", fallbackMethod="fallbackUpdateSale")
    @Retry(name="shoppingCart-service")
    public Optional<Sale> updateSale(Long id, Sale sale) {

        Optional<Sale> findSale =  saleRepo.findById(id);

        if (findSale.isPresent()) {
            Sale saleNew=findSale.get();

            //Find shopping cart with Feing
            ShoppingCartDTO shoppingCarts = shoppingCartAPI.getShoppingCart(sale.getIdShoppingCart());

            saleNew.setDateSale(sale.getDateSale());
            saleNew.setIdShoppingCart(shoppingCarts.getId());

            return Optional.of(saleRepo.save(saleNew));

        }

        return Optional.empty();
    }

    @Override
    public String deleteSale(Long id) {
        if(saleRepo.findById(id).isPresent()){
            saleRepo.deleteById(id);
            return "Sale deleted";
        }
        return "Sale not found";
    }

    @Override
    @CircuitBreaker(name="shoppingCart-service", fallbackMethod="fallbackFindByIdSale")
    @Retry(name="shoppingCart-service")
    public Optional<SaleDTO> findByIdSale(Long id) {

        //Find sale
        Optional<Sale> findSale =  saleRepo.findById(id);

        if (findSale.isPresent()) {

            Sale sale=findSale.get();

            //Ids for the shopping cart
            Long idShoppingCart = sale.getIdShoppingCart();

            //Find shopping cart with Feing
            ShoppingCartDTO shoppingCarts = shoppingCartAPI.getShoppingCart(idShoppingCart);

            SaleDTO saleDTO = new SaleDTO();

            saleDTO.setId(id);
            saleDTO.setDateSale(sale.getDateSale());
            saleDTO.setShoppingCart(shoppingCarts);

            return Optional.of(saleDTO);

        }

        return Optional.empty();
    }

    @Override
    @CircuitBreaker(name="shoppingCart-service", fallbackMethod="fallbackFindAllSale")
    @Retry(name="shoppingCart-service")
    public List<SaleDTO> findAllSale() {

        List<Sale> listSale = saleRepo.findAll();

        List<SaleDTO> listSaleDTO = new ArrayList<>();

        listSale.forEach(sale-> {

            SaleDTO saleDTO = new SaleDTO();

            //Ids for the shopping cart
            Long idShoppingCart = sale.getIdShoppingCart();

            //Find shopping cart with Feing
            ShoppingCartDTO shoppingCarts = shoppingCartAPI.getShoppingCart(idShoppingCart);

            saleDTO.setId(sale.getId());
            saleDTO.setDateSale(sale.getDateSale());
            saleDTO.setShoppingCart(shoppingCarts);

            listSaleDTO.add(saleDTO);
        });

        return listSaleDTO;
    }

    //Circuit Breaker for save Sale
    public Sale fallbackSaveSale(Throwable throwable) {
        throw new ServiceUnavailableException("Not save Sale. Try again.");
    }

    //Circuit Breaker for update Sale
    public Optional<Sale> fallbackUpdateSale(Throwable throwable) {
        throw new ServiceUnavailableException("Not update Sale. Try again.");
    }

    //Circuit Breaker for find one Sale
    public Optional<SaleDTO> fallbackFindByIdSale(Throwable throwable) {
        throw new ServiceUnavailableException("Not find Sale. Try again.");
    }

    //Circuit Breaker for find all Sale
    public List<SaleDTO> fallbackFindAllSale(Throwable throwable) {
        throw new ServiceUnavailableException("Not find all Sale. Try again.");
    }


}
