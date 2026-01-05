package com.example.productcatalog.controller;

import com.example.productcatalog.dto.request.ProductRequest;
import com.example.productcatalog.dto.response.ProductResponse;
import com.example.productcatalog.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // CREATE
    @PostMapping
    public ProductResponse create(@RequestBody @Valid ProductRequest req) {
        return productService.create(req);
    }

    // READ: get by id
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    // READ: get all (pagination + sort)
    // /api/products/all?page=0&size=10&sort=id,desc
    @GetMapping("/all")
    public Page<ProductResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        Pageable pageable = buildPageable(page, size, sort);
        return productService.getAll(pageable);
    }

    // READ: filter (custom query + pagination + sort)
    // /api/products?categoryId=&minPrice=&maxPrice=&page=0&size=10&sort=price,asc
    @GetMapping
    public Page<ProductResponse> filter(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        Pageable pageable = buildPageable(page, size, sort);
        return productService.filter(categoryId, minPrice, maxPrice, pageable);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Integer id, @RequestBody @Valid ProductRequest req) {
        return productService.update(id, req);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.delete(id);
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(dir, field));
    }
}



