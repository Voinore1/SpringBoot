package org.example.controller;

import org.example.dtos.CategoryPostDto;
import org.example.entities.CategoryEntity;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryEntity> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable Integer id) {
        Optional<CategoryEntity> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)


    public ResponseEntity<CategoryEntity> createCategory(@ModelAttribute CategoryPostDto category) {
        CategoryEntity createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCategory(@PathVariable Integer id, @ModelAttribute CategoryPostDto category) {
        return categoryService.updateCategory(id, category)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }



}
