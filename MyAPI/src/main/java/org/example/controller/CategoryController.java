package org.example.controller;

import org.example.entities.CategoryEntity;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<CategoryEntity> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<CategoryEntity> getCategory(@PathVariable("id") int id) { return categoryService.getCategoryById(id);}

    @PostMapping
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryEntity entity) {
        categoryService.addCategory(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryEntity> updateCategory(
            @PathVariable("id") int id,
            @RequestBody CategoryEntity updatedCategory
    ) {
        if (categoryService.getCategoryById(id).isPresent()) {
            updatedCategory.setId(id);
            categoryService.updateCategory(updatedCategory);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") int id) {
        if (categoryService.getCategoryById(id).isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
