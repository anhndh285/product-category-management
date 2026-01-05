package com.example.productcatalog.dto.response;

import com.example.productcatalog.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Integer id;
    private String name;

    public CategoryResponse(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }
}
