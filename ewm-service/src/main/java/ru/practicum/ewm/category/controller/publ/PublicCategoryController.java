package ru.practicum.ewm.category.controller.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.publ.CategoryDto;
import ru.practicum.ewm.category.service.publ.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequestMapping("/categories")
@RestController
@RequiredArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService service;

    @GetMapping
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
            @Positive @RequestParam(defaultValue = "10") Long size
    ) {
        log.info("Request to receive categories by user.");
        return service.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@Positive @PathVariable Long catId) {
        log.info("Request to receive category {} by user", catId);
        return service.getCategory(catId);
    }
}