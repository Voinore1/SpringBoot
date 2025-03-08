package org.example.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductPostDto {
    private String name;
    private float price;
    private int amount;
    private int categoryId;
    private List<MultipartFile> imageFiles;
    private String description;
}
