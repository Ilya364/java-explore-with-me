package ru.practicum.statistics.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.statistics.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHit dto) {
        System.out.println("dto = " + dto);
        log.info("Request to record a hit for \"{}\".", dto.getUri());
        statsService.saveHit(dto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam (required = false) List<String> uris,
            @RequestParam (defaultValue = "false") Boolean unique
    ) {
        log.info("Request to get stats for \"{}\".", uris);
        return statsService.getStats(start, end, uris, unique);
    }
}
