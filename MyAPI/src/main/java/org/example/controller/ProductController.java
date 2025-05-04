package org.example.controller;

import org.example.Mappers.ProductMapper;
import org.example.dtos.ProductGetDto;
import org.example.dtos.ProductPostDto;
import org.example.entities.ProductEntity;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public List<ProductGetDto> getAllProducts() { return productMapper.toDto(productService.getAllProducts()); }

    @GetMapping("/{id}")
    public ResponseEntity<ProductGetDto> getProductById(@PathVariable int id){
        var productDto = productService.getProductById(id);
        return productDto != null
                ? new ResponseEntity<>(productDto, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductGetDto> createProduct(@ModelAttribute ProductPostDto product){
        ProductGetDto createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCategory(@PathVariable int id, @ModelAttribute ProductPostDto product){
        return productService.updateProduct(id, product)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable int id){
        return productService.deleteProduct(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
