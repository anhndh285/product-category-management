package com.example.productcatalog.service;

import com.example.productcatalog.dto.request.ProductRequest;
import com.example.productcatalog.dto.response.ProductResponse;
import com.example.productcatalog.entity.Category;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.exception.NotFoundException;
import com.example.productcatalog.repository.CategoryRepository;
import com.example.productcatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // CREATE
    public ProductResponse create(ProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + req.getCategoryId()));

        Product p = new Product();
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setQuantity(req.getQuantity());
        p.setCategory(category);

        return toResponse(productRepository.save(p));
    }

    // READ: get by id
    public ProductResponse getById(Integer id) {
        Product p = productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
        return toResponse(p);
    }

    // READ: get all (pagination + sort)
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    // READ: filter (custom query + pagination + sort)
    public Page<ProductResponse> filter(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.filter(categoryId, minPrice, maxPrice, pageable).map(this::toResponse);
    }

    // UPDATE
    public ProductResponse update(Integer id, ProductRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + req.getCategoryId()));

        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setQuantity(req.getQuantity());
        p.setCategory(category);

        return toResponse(productRepository.save(p));
    }

    // DELETE
    public void delete(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    // Mapper: Entity -> DTO
    private ProductResponse toResponse(Product p) {
        ProductResponse r = new ProductResponse(p);
        r.setId(p.getId());
        r.setName(p.getName());
        r.setPrice(p.getPrice());
        r.setQuantity(p.getQuantity());

        // cần categoryId + categoryName
        r.setCategoryId(p.getCategory().getId());
        r.setCategoryName(p.getCategory().getName());
        return r;
    }
}


