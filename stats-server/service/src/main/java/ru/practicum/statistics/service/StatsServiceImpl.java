package ru.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.statistics.model.Hit;
import ru.practicum.statistics.model.HitStatsMapper;
import ru.practicum.statistics.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        Hit hit = HitStatsMapper.toHit(endpointHit);
        repository.save(hit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<Hit> hits;
        if (uris == null) {
            hits = repository.findAllByTimestampBetween(start, end);
        } else {
            hits = repository.findAllByTimestampBetweenAndUriIn(start, end, uris);
        }
        if (unique && !hits.isEmpty()) {
            hits = filterByUniqueIps(hits);
        }

        return buildViewStats(hits);
    }

    private List<Hit> filterByUniqueIps(List<Hit> hits) {
        return new ArrayList<>(
            hits.stream()
                .collect(Collectors.toMap(Hit::getIp, h -> h, (existing, replacement) -> existing))
                .values()
        );
    }

    private List<ViewStats> buildViewStats(List<Hit> hits) {
        Map<String, ViewStats> stats = new HashMap<>();
        for (Hit hit : hits) {
            ViewStats viewStats = stats.getOrDefault(
                    hit.getUri(), ViewStats.builder()
                            .app(hit.getApp())
                            .uri(hit.getUri())
                            .hits(0L)
                            .build()
            );
            viewStats.incHits();
            stats.put(hit.getUri(), viewStats);
        }
        List<ViewStats> statsList = new ArrayList<>(stats.values());
        statsList.sort(Comparator.comparing(ViewStats::getHits).reversed());
        return statsList;
    }
}
