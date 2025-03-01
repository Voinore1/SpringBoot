package org.example.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryPostDto {
    private String name;
    private String description;
    private MultipartFile imageFile;
}