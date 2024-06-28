package ru.practicum.ewm.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.admin.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.admin.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.dto.publ.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventDtoMapper;

@UtilityClass
public class CompilationDtoMapper {
    public CompilationDto toDto(Compilation compilation) {
        CompilationDto dto = CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
        if (compilation.getEvents() != null) {
            dto.setEvents(EventDtoMapper.toEventShortDtoList(compilation.getEvents()));
        }
        return dto;
    }

    public Compilation toCompilation(NewCompilationDto dto) {
        Compilation compilation = Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        return compilation;
    }

    public void partialMapToCompilation(Compilation compilation, UpdateCompilationRequest request) {
        if (compilation.getTitle() != null) {
            compilation.setTitle(compilation.getTitle());
        }
        if (compilation.getPinned() != null) {
            compilation.setPinned(compilation.getPinned());
        }
    }
}
