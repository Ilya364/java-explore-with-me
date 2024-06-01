package ru.practicum.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.statistics.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    List<Hit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
