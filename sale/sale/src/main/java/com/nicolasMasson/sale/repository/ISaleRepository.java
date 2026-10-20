package com.nicolasMasson.sale.repository;

import com.nicolasMasson.sale.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {
}
