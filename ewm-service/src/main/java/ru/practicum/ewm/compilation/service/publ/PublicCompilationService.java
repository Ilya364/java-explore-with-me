package ru.practicum.ewm.compilation.service.publ;

import ru.practicum.ewm.compilation.dto.publ.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Long from, Long size);

    CompilationDto getCompilation(Long compId);
}
