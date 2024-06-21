package ru.practicum.ewm.compilation.controller.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.publ.CompilationDto;
import ru.practicum.ewm.compilation.service.publ.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequestMapping("/compilations")
@RestController
@RequiredArgsConstructor
public class PublicCompilationController {
    private final PublicCompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
            @Positive @RequestParam(defaultValue = "10") Long size
    ) {
        log.info("Request to get compilations by user.");
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(
            @Positive @PathVariable Long compId
    ) {
        log.info("Request to get compilation {} by user.", compId);
        return service.getCompilation(compId);
    }
}
