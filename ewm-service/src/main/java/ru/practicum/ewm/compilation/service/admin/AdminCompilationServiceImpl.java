package ru.practicum.ewm.compilation.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.admin.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.admin.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.dto.publ.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.error.exception.DuplicateException;
import ru.practicum.ewm.error.exception.NotEmptyException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

import static ru.practicum.ewm.compilation.dto.CompilationDtoMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto dto) {
        try {
            Compilation compilation = toCompilation(dto);
            if (dto.getEvents() != null) {
                List<Event> compilationEvents = eventRepository.findAllById(dto.getEvents());
                compilation.addEvents(compilationEvents);
            }
            CompilationDto result = toDto(compilationRepository.save(compilation));
            log.info("Compilation '{}' created.", dto.getTitle());
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(String.format("Category with title '%s' already exists", dto.getTitle()));
        }
    }

    @Override
    public void deleteCompilation(Long compId) {
        try {
            if (compilationRepository.existsById(compId)) {
                compilationRepository.deleteById(compId);
                log.info("Compilation '{}' deleted.", compId);
            } else {
                throw new NotFoundException(String.format("Compilation with id %d not found.", compId));
            }
        } catch (DataIntegrityViolationException e) {
            throw new NotEmptyException(String.format("Compilation with id %d not empty.", compId));
        }
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation forUpdateCompilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id %d not found.", compId))
        );

        forUpdateCompilation.removeAll();
        if (request.getEvents() != null) {
            List<Event> newEvents = eventRepository.findAllById(request.getEvents());
            forUpdateCompilation.addEvents(newEvents);
        }
        partialMapToCompilation(forUpdateCompilation, request);
        compilationRepository.save(forUpdateCompilation);
        log.info("Compilation {} updated.", compId);

        return toDto(forUpdateCompilation);
    }
}
