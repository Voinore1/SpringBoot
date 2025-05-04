package org.example.service;

import lombok.AllArgsConstructor;
import org.example.Mappers.ProductMapper;
import org.example.dtos.ProductGetDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    //Автоматично робиться Dependency Injection -
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductImageRepository productImageRepository;
    private FileService fileService;
    private ProductMapper mapper;

    public List<ProductEntity> getAllProducts() { return productRepository.findAll(); }

    public ProductGetDto getProductById(int id) { var res = productRepository.findById(id);
        return res.isPresent()
                ? mapper.toDto(res.get())
                : null;
    }

    @Transactional
    public ProductGetDto createProduct(ProductPostDto product) {
        var entity = new ProductEntity();
        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        entity.setAmount(product.getAmount());
        entity.setDescription(product.getDescription());
        entity.setCreationTime(LocalDateTime.now());

        var categoryId = product.getCategoryId();
        if (categoryRepository.existsById(categoryId)){
            var category = new CategoryEntity();
            category.setId(categoryId);
            entity.setCategory(category);
        }
        productRepository.save(entity);

        var imageFiles = product.getImageFiles();
        if (imageFiles != null) {
            var priority = 1;
            for (var file : imageFiles) {
                if (file == null || file.isEmpty()) continue;
                var imageName = fileService.load(file);
                var img = new ProductImageEntity();
                img.setPriority(priority++);
                img.setImageURL(imageName);
                img.setProduct(entity);
                productImageRepository.save(img);
            }
        }

        return mapper.toDto(entity);
    }

    @Transactional
    public boolean updateProduct(int id, ProductPostDto product) {
        var entity = productRepository.findById(id).orElseThrow();

        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setAmount(product.getAmount());
        entity.setPrice(product.getPrice());

        var category = new CategoryEntity();
        category.setId(product.getCategoryId());
        entity.setCategory(category);
        productRepository.save(entity);

        // Отримуємо список старих зображень у базі
        Map<String, ProductImageEntity> existingImages = entity.getImages().stream()
                .collect(Collectors.toMap(ProductImageEntity::getImageURL, img -> img));

        List<ProductImageEntity> updatedImages = new ArrayList<>();
        var productFilesCount = product.getImageFiles() == null ? 0 : product.getImageFiles().size();

        for (int i = 0; i < productFilesCount; i++) {
            var img = product.getImageFiles().get(i);

            if ("old-image".equals(img.getContentType())) {
                // Оновлення пріоритету старого зображення
                var imageName = img.getOriginalFilename();
                if (existingImages.containsKey(imageName)) {
                    var oldImage = existingImages.get(imageName);
                    oldImage.setPriority(i + 1);
                    productImageRepository.save(oldImage);
                    updatedImages.add(oldImage);
                }
            } else {
                // Додавання нового зображення
                var imageName = fileService.load(img);
                var newImage = new ProductImageEntity();
                newImage.setImageURL(imageName);
                newImage.setPriority(i + 1);
                newImage.setProduct(entity);
                productImageRepository.save(newImage);
            }
        }
        List<Integer> removeIds = new ArrayList<>();
        // Видалення зображень, яких немає в оновленому списку
        for (var img : entity.getImages()) {
            if (!updatedImages.contains(img)) {
                fileService.remove(img.getImageURL());
                removeIds.add(img.getId());
            }
        }
        productImageRepository.deleteAllByIdInBatch(removeIds);
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
        productImages.forEach(image -> fileService.remove(image.getImageURL()));

        // Видаляємо сам продукт
        productRepository.delete(product);
        return true;
    }
}
