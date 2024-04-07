package edu.ntnu.idatt2105.backend.mapper.category;

import edu.ntnu.idatt2105.backend.dto.category.CategoryDTO;
import edu.ntnu.idatt2105.backend.dto.category.MultipleCategoryDTO;
import edu.ntnu.idatt2105.backend.model.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This mapper converts between the Category model and its DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
@Mapper()
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    default MultipleCategoryDTO categoriesToMultipleCategoryDTO(List<Category> categories) {
        return MultipleCategoryDTO
                .builder()
                .categories(categories.stream().map(this::categoryToCategoryDTO).collect(Collectors.toList()))
                .build();
    }

    CategoryDTO categoryToCategoryDTO(Category category);

}
