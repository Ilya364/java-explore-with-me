package ru.practicum.ewm.compilation.service.admin;

import ru.practicum.ewm.compilation.dto.admin.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.admin.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.dto.publ.CompilationDto;

public interface AdminCompilationService {
    CompilationDto createCompilation(NewCompilationDto dto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compID, UpdateCompilationRequest request);
}
