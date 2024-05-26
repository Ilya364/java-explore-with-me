package ru.practicum.statistics.service;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.util.List;

public interface StatsService {
    void saveHit(EndpointHit endpointHit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
