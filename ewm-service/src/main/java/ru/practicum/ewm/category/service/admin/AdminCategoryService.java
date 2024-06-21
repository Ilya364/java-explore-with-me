package ru.practicum.ewm.category.service.admin;

import ru.practicum.ewm.category.dto.admin.NewCategoryDto;
import ru.practicum.ewm.category.dto.publ.CategoryDto;

public interface AdminCategoryService {
    CategoryDto createCategory(NewCategoryDto dto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(NewCategoryDto dto, Long categoryId);
}
