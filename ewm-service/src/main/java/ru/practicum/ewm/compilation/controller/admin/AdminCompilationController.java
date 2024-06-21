package ru.practicum.ewm.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.admin.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.admin.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.dto.publ.CompilationDto;
import ru.practicum.ewm.compilation.service.admin.AdminCompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RequestMapping("/admin/compilations")
@RestController
@RequiredArgsConstructor
public class AdminCompilationController {
    private final AdminCompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto dto) {
        log.info("Request to create compilation.");
        return service.createCompilation(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable Long compId) {
        log.info("Request to delete compilation {}.", compId);
        service.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @Positive @PathVariable Long compId,
            @Valid @RequestBody UpdateCompilationRequest request
    ) {
        log.info("Request to update compilation {}.", compId);
        return service.updateCompilation(compId, request);
    }
}
