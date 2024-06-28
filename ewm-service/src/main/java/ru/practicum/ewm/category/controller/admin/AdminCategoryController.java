package ru.practicum.ewm.category.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.admin.NewCategoryDto;
import ru.practicum.ewm.category.dto.publ.CategoryDto;
import ru.practicum.ewm.category.service.admin.AdminCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RequestMapping("/admin/categories")
@RestController
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto dto) {
        log.info("Request to create category.");
        return adminCategoryService.createCategory(dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable Long catId) {
        log.info("Request to delete category {}.", catId);
        adminCategoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @Valid @RequestBody NewCategoryDto dto,
            @Positive @PathVariable Long catId
    ) {
        log.info("Request to update category {}.", catId);
        return adminCategoryService.updateCategory(dto, catId);
    }
}
