package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dtos.ProductPostDto;
import org.example.entities.CategoryEntity;
import org.example.entities.ProductEntity;
import org.example.entities.ProductImageEntity;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductImageRepository;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    //Автоматично робиться Dependency Injection -
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductImageRepository productImageRepository;
    private FileService fileService;

    public List<ProductEntity> getAllProducts() { return productRepository.findAll(); }

    public Optional<ProductEntity> getProductById(int id) { return productRepository.findById(id);}

    @Transactional
    public ProductEntity createProduct(ProductPostDto product) {
        var category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        var entity = new ProductEntity();
        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        entity.setAmount(product.getAmount());
        entity.setDescription(product.getDescription());
        entity.setCreationTime(LocalDateTime.now());
        entity.setCategory(category);

        productRepository.save(entity);

        var imageFiles = product.getImageFiles();
        if (imageFiles != null) {
            List<ProductImageEntity> images = imageFiles.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .map(file -> {
                        var imageName = fileService.load(file);
                        var img = new ProductImageEntity();
                        img.setName(imageName);
                        img.setProduct(entity);
                        return img;
                    })
                    .toList();
            productImageRepository.saveAll(images);
        }

        return entity;
    }

    @Transactional
    public boolean updateProduct(int id, ProductPostDto updatedProduct) {
        var productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) return false;

        var product = productOpt.get();

        var category = categoryRepository.findById(updatedProduct.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        product.setCategory(category);

        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setAmount(updatedProduct.getAmount());
        product.setDescription(updatedProduct.getDescription());

        var oldImages = productImageRepository.findByProduct(product);
        productImageRepository.deleteAll(oldImages);
        oldImages.forEach(image -> fileService.remove(image.getName()));

        var newImages = updatedProduct.getImageFiles();
        if (newImages != null) {
            List<ProductImageEntity> images = newImages.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .map(file -> {
                        var imageName = fileService.load(file);
                        var img = new ProductImageEntity();
                        img.setName(imageName);
                        img.setProduct(product);
                        return img;
                    })
                    .toList();
            productImageRepository.saveAll(images);
        }

        productRepository.save(product);
        return true;
    }

    @Transactional
    public boolean deleteProduct(int id) {
        var productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) return false;

        var product = productOpt.get();

        // Видаляємо всі зображення перед видаленням продукту
        var productImages = productImageRepository.findByProduct(product);
        productImageRepository.deleteAll(productImages);
        productImages.forEach(image -> fileService.remove(image.getName()));

        // Видаляємо сам продукт
        productRepository.delete(product);
        return true;
    }
}
