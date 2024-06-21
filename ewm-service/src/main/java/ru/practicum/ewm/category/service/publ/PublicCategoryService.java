package ru.practicum.ewm.category.service.publ;

import ru.practicum.ewm.category.dto.publ.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> getCategories(Long from, Long size);

    CategoryDto getCategory(Long id);
}
