package ru.practicum.ewm.category.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.admin.NewCategoryDto;
import ru.practicum.ewm.category.dto.publ.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.exception.DuplicateException;
import ru.practicum.ewm.error.exception.NotEmptyException;
import ru.practicum.ewm.error.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import static ru.practicum.ewm.category.dto.CategoryDtoMapper.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;
    @PersistenceContext
    private final EntityManager entityManager;

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
            entityManager.flush();
            log.info("Category {} deleted.", categoryId);
        } catch (PersistenceException e) {
            throw new NotEmptyException(String.format("The category with id %d not empty.", categoryId));
        }
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto dto, Long categoryId) {
        try {
            Category forUpdate = getCategoryById(categoryId);
            forUpdate.setName(dto.getName());
            entityManager.flush();
            log.info("Category {} updated.", categoryId);
            return toCategoryDto(forUpdate);
        } catch (PersistenceException e) {
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
