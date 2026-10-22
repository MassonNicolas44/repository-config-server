package com.nicolasMasson.sale.controller;

import com.nicolasMasson.sale.dto.SaleDTO;
import com.nicolasMasson.sale.model.Sale;
import com.nicolasMasson.sale.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private ISaleService saleService;

    @PostMapping("/save")
    public Sale saleSave(@RequestBody Sale sale){
        return saleService.saveSale(sale);
    }

    @GetMapping("/find/{id}")
    public Object findSale(@PathVariable Long id){
        Optional<SaleDTO> findSale=saleService.findByIdSale(id);

        if(findSale.isPresent()){
            return findSale.get();
        }

        return "Sale not found";
    }
    
    @GetMapping("/findAll")
    public List<SaleDTO> findAllSale(){
        return saleService.findAllSale();
    }

    @PutMapping("/update/{id}")
    public Object updateSale(@RequestBody Sale sale, @PathVariable Long id){
        Optional<SaleDTO> findSale=saleService.findByIdSale(id);

        if(findSale.isPresent()){
            saleService.updateSale(id,sale);
            return findSale.get();
        }
        return "Sale not found";
    }


    @DeleteMapping("/delete/{id}")
    public Object deleteSale(@PathVariable Long id){
        Optional<SaleDTO> findSale=saleService.findByIdSale(id);
        if(findSale.isPresent()){
            saleService.deleteSale(id);
            return "Sale deleted";
        }
        return "Sale not found";
    }

}
