package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    @Value("${upload.dir}")
    private String uploadDir;

    public String load(MultipartFile file) {
        if (file.isEmpty()) return "";

        try (var inputStream = file.getInputStream()) {
            Files.createDirectories(Paths.get(uploadDir));

            var fileName = file.getOriginalFilename();
            var fileExt = fileName != null && fileName.contains(".")
                    ? fileName.substring(fileName.lastIndexOf("."))
                    : "";
            var newFileName = UUID.randomUUID() + fileExt;
            Path filePath = Paths.get(uploadDir, newFileName);

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public String load(String imageUrl) {
        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://"))
            return "";

        try (var inputStream = new URL(imageUrl).openStream()) {
            Files.createDirectories(Paths.get(uploadDir));

            var fileExt = imageUrl.contains(".")
                    ? imageUrl.substring(imageUrl.lastIndexOf("."))
                    : "";
            var newFileName = UUID.randomUUID() + fileExt;
            Path filePath = Paths.get(uploadDir, newFileName);

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void remove(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String replace(String oldFileName, MultipartFile newFile) {
        var newFileName = load(newFile);
        if (newFileName == "") {
            return oldFileName;
        }

        remove(oldFileName);
        return newFileName;
    }


}