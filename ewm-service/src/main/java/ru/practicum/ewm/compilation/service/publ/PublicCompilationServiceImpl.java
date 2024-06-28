package ru.practicum.ewm.compilation.service.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDtoMapper;
import ru.practicum.ewm.compilation.dto.publ.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.event.dto.EventDtoMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(
                pinned, PageRequest.of(from / size, size)
        );
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            CompilationDto compilationDto = CompilationDtoMapper.toDto(compilation);
            compilationDto.setEvents(EventDtoMapper.toEventShortDtoList(compilation.getEvents()));
            result.add(compilationDto);
        }
        log.info("Compilations received by user.");
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id %d not found.", compId))
        );
        CompilationDto result = CompilationDtoMapper.toDto(compilation);
        log.info("Compilation {} received by user.", compId);
        return result;
    }
}
