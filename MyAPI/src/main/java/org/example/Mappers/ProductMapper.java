package org.example.Mappers;

import org.example.dtos.ProductGetDto;
import org.example.entities.ProductEntity;
import org.example.entities.ProductImageEntity;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "imageLinks", source = "images", qualifiedByName = "mapImageLinks")
    ProductGetDto toDto(ProductEntity productEntity);

    @Named("mapImageLinks")
    default List<String> mapImageLinks(List<ProductImageEntity> images) {
        if (images == null) {
            return Collections.emptyList();
        }
        return images.stream()
                .map(ProductImageEntity::getImageURL)
                .collect(Collectors.toList());
    }

    List<ProductGetDto> toDto(List<ProductEntity> productEntities);
}
