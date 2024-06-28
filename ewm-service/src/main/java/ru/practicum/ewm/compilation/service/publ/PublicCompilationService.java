package ru.practicum.ewm.compilation.service.publ;

import ru.practicum.ewm.compilation.dto.publ.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);
}
