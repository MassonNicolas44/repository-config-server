package com.nicolasMasson.shoppingCart.model;

import com.nicolasMasson.shoppingCart.dto.ProductCart;
import com.nicolasMasson.shoppingCart.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalPrice;

    @Transient
    private List<ProductDTO> listProduct;

    @ElementCollection
    private List<ProductCart> productsCart;

}
