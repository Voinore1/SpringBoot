package org.example.seeder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component
public class ImageSeeder implements CommandLineRunner {

    @Value("${image.directory:Images}")
    private String imageDirectory;

    @Override
    public void run(String... args) throws Exception {
        List<String> imageUrls = List.of(
                "https://images.unsplash.com/photo-1718821378542-c53d72157628",
                "https://images.unsplash.com/photo-1728606987255-920740a48fe5",
                "https://images.unsplash.com/photo-1660552085347-46e71e3784a6",
                "https://images.unsplash.com/photo-1692185175217-6714aefe7ac9",
                "https://images.unsplash.com/photo-1696176765041-1ab9ee1f684e",
                "https://images.unsplash.com/photo-1650783804661-354f64512c39"
        );

        Path directoryPath = Paths.get(imageDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        for (String imageUrl : imageUrls) {
            downloadImage(imageUrl, directoryPath);
        }
    }

    private void downloadImage(String imageUrl, Path directoryPath) {
        try {
            URL url = new URL(imageUrl);

            URLConnection connection = url.openConnection();
            String contentType = connection.getContentType();
            String extension = getFileExtension(contentType);

            InputStream inputStream = url.openStream();

            String fileName = "photo_" + UUID.randomUUID().toString() + extension;

            Files.copy(inputStream, directoryPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            inputStream.close();

            System.out.println("Downloaded: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to download image: " + imageUrl);
        }
    }

    private static String getFileExtension(String contentType) {
        if (contentType != null) {
            switch (contentType) {
                case "image/jpeg":
                    return ".jpg";
                case "image/png":
                    return ".png";
                case "image/gif":
                    return ".gif";
                case "image/bmp":
                    return ".bmp";
                case "image/webp":
                    return ".webp";
                default:
                    return ".jpg";  // За замовчуванням jpg
            }
        }
        return ".jpg";  // Якщо MIME тип не визначений
    }
}
