package com.example.productcatalog.dto.response;

import com.example.productcatalog.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    private Integer categoryId;
    private String categoryName;

    public ProductResponse(Product p){
        this.id = p.getId();
        this.name = p.getName();
        this.price = p.getPrice();
        this.quantity = p.getQuantity();
        this.categoryId = p.getCategory().getId();
        this.categoryName = p.getCategory().getName();
    }
}
