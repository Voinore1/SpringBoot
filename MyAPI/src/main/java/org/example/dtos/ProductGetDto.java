package org.example.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductGetDto {
    private int id;
    private LocalDateTime creationTime;
    private String name;
    private float price;
    private int amount;
    private int categoryId;
    private String categoryName;
    private List<String> imageLinks;
    private String description;
}
