package ru.practicum.ewm.category.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.admin.NewCategoryDto;
import ru.practicum.ewm.category.dto.publ.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.exception.DuplicateException;
import ru.practicum.ewm.error.exception.NotEmptyException;
import ru.practicum.ewm.error.exception.NotFoundException;

import static ru.practicum.ewm.category.dto.CategoryDtoMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        Category category = toCategory(dto);
        try {
            CategoryDto result = toCategoryDto(repository.save(category));
            log.info("Category '{}' created.", result.getName());
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(
                    String.format("Category with name '%s' already exists", dto.getName())
            );
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!repository.existsById(categoryId)) {
            throw new NotFoundException(String.format("Category %d not found.", categoryId));
        }
        try {
            repository.deleteById(categoryId);
            log.info("Category {} deleted.", categoryId);
        } catch (DataIntegrityViolationException e) {
            throw new NotEmptyException(String.format("The category with id %d not empty.", categoryId));
        }
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto dto, Long categoryId) {
        Category forUpdate = getCategoryById(categoryId);
        try {
            forUpdate.setName(dto.getName());
            repository.save(forUpdate);
            log.info("Category {} updated.", categoryId);
            return toCategoryDto(forUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(
                    String.format("Category with name '%s' already exists", dto.getName())
            );
        }
    }

    private Category getCategoryById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %d not found.", id))
        );
    }
}
