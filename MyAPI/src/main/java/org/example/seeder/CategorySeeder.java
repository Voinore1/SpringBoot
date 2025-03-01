package org.example.seeder;

import org.example.entities.CategoryEntity;
import org.example.repository.CategoryRepository;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class CategorySeeder {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    public void seed() {
        if (categoryRepository.count() > 0) return;

        var category1 = new CategoryEntity();
        category1.setName("Electronics");
        category1.setDescription("Gadgets and devices");
        var imageName = fileService
                .load("https://www.shift4shop.com/2015/images/sell-online/electronics/selling-charge-up.jpg");
        category1.setImage(imageName);
        category1.setCreationTime(LocalDateTime.now());

        var category2 = new CategoryEntity();
        category2.setName("Books");
        category2.setDescription("All kinds of books");
        imageName = fileService
                .load("https://elearningindustry.com/wp-content/uploads/2016/05/top-10-books-every-college-student-read-e1464023124869.jpeg");
        category2.setImage(imageName);
        category2.setCreationTime(LocalDateTime.now());

        var category3 = new CategoryEntity();
        category3.setName("Clothing");
        category3.setDescription("Fashion and apparel");
        imageName = fileService
                .load("https://www.revivingsimple.com/wp-content/uploads/2020/03/not-buying-new-clothes-for-a-year-bar.jpg");
        category3.setImage(imageName);
        category3.setCreationTime(LocalDateTime.now());

        // Зберігаємо дані до бази
        categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
    }
}
