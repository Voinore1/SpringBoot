package org.example.seeder;

import org.example.entities.ProductEntity;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@Component
public class ProductSeeder {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    public void seed() {
        if(productRepository.count() > 0) return;

        var categories = categoryRepository.findAll();
        var random = new Random();

        var product1 = new ProductEntity();
        product1.setName("IPhone 16");
        product1.setDescription("8/256");
        var imageName = fileService
                .load("https://estore.ua/media/catalog/product/cache/8/image/650x650/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone-16-pro-finish-select-202409-6-3inch-naturaltitanium.png");
        product1.setImage(imageName);
        product1.setCreationTime(LocalDateTime.now());
        product1.setAmount(5);
        product1.setPrice(1000);
        product1.setCategory(categories.get(random.nextInt(categories.size())));

        var product2 = new ProductEntity();
        product2.setName("Jacket");
        product2.setDescription("very nice");
        imageName = fileService
                .load("https://parkas.com.ua/wa-data/public/shop/products/34/01/134/images/377/377.970.jpg");
        product2.setImage(imageName);
        product2.setCreationTime(LocalDateTime.now());
        product2.setAmount(12);
        product2.setPrice(150);
        product2.setCategory(categories.get(random.nextInt(categories.size())));

        var product3 = new ProductEntity();
        product3.setName("C++");
        product3.setDescription("good book");
        imageName = fileService
                .load("https://images.booksense.com/images/740/539/9783986539740.jpg");
        product3.setImage(imageName);
        product3.setCreationTime(LocalDateTime.now());
        product3.setAmount(7);
        product3.setPrice(20);
        product3.setCategory(categories.get(random.nextInt(categories.size())));

        // Зберігаємо дані до бази
        productRepository.saveAll(Arrays.asList(product1, product2, product3));
    }
}