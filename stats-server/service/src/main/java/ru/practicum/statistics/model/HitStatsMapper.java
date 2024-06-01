package ru.practicum.statistics.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHit;

@UtilityClass
public class HitStatsMapper {
    public Hit toHit(EndpointHit dto) {
        return Hit.builder()
                .ip(dto.getIp())
                .uri(dto.getUri())
                .app(dto.getApp())
                .timestamp(dto.getHitTimestamp())
                .build();
    }
}
