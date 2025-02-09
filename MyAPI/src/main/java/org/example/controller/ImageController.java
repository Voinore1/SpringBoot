package org.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${image.directory:Images}")
    private String imageDirectory;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(imageDirectory).resolve(filename).normalize();
            System.out.println(imagePath);
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }
            Resource resource = new UrlResource(imagePath.toUri());

            // Визначаємо тип вмісту зображення
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // за замовчуванням
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))  // Встановлюємо Content-Type
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
