package ru.practicum.ewm.category.service.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.publ.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.exception.NotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ru.practicum.ewm.category.dto.CategoryDtoMapper.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(Long from, Long size) {
        List<CategoryDto> result = toCategoryDtoList(repository.findAllByIdBetween(from, from + size));
        log.info("Categories received by user.");
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategory(Long id) {
        Category result = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %s not found", id))
        );
        log.info("Category {} received by user.", id);
        return toCategoryDto(result);
    }
}
