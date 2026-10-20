package com.nicolasMasson.sale.service;

import com.nicolasMasson.sale.dto.SaleDTO;
import com.nicolasMasson.sale.model.Sale;

import java.util.List;
import java.util.Optional;

public interface ISaleService {

    Sale saveSale(Sale sale);

    Optional<Sale> updateSale(Long id,Sale sale);

    String deleteSale(Long id);

    Optional<SaleDTO> findByIdSale(Long id);
    List<SaleDTO> findAllSale();


}
