package org.example.service;

import org.example.dtos.CategoryPostDto;
import org.example.entities.CategoryEntity;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    //Автоматично робиться Dependency Injection -
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    public List<CategoryEntity> getAllCategories() { return categoryRepository.findAll(); }

    public Optional<CategoryEntity> getCategoryById(int id) { return categoryRepository.findById(id);}

    public CategoryEntity createCategory(CategoryPostDto category) {
        var entity = new CategoryEntity();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setCreationTime(LocalDateTime.now());

        var imagePath = fileService.load(category.getImageFile());
        entity.setImage(imagePath);
        return categoryRepository.save(entity);
    }

    public boolean updateCategory(Integer id, CategoryPostDto category) {
        var res = getCategoryById(id);
        if (res.isEmpty()){
            return false;
        }

        var entity = res.get();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());

        var newImageFile = category.getImageFile();
        if (!newImageFile.isEmpty()){
            var newImagePath = fileService.replace(entity.getImage(), category.getImageFile());
            entity.setImage(newImagePath);
        }
        categoryRepository.save(entity);
        return true;
    }

    public boolean deleteCategory(Integer id) {
        var res = getCategoryById(id);
        if (res.isEmpty()){
            return false;
        }
        fileService.remove(res.get().getImage());
        categoryRepository.deleteById(id);
        return true;
    }
}
