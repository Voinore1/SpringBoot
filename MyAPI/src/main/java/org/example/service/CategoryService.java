package org.example.service;

import org.example.entities.CategoryEntity;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    //Автоматично робиться Dependency Injection -
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() { return categoryRepository.findAll(); }
    public Optional<CategoryEntity> getCategoryById(int id) { return categoryRepository.findById(id);}
    public void addCategory(CategoryEntity entity) { categoryRepository.save(entity); }
    public void updateCategory(CategoryEntity entity){
        // Ensure the entity exists in the database before updating
        if (entity.getId() != null && categoryRepository.existsById(entity.getId())) {
            categoryRepository.save(entity);
        } else {
            throw new RuntimeException("Category with ID " + entity.getId() + " does not exist.");
        }
    }
    public void deleteCategory(int id) { categoryRepository.deleteById(id); }
}
