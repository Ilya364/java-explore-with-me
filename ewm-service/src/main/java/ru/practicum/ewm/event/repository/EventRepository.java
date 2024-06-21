package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query(value = "SELECT * FROM events e " +
            "WHERE e.initiator_id = ?1 " +
            "ORDER BY e.id ASC " +
            "LIMIT ?3 OFFSET ?2 ", nativeQuery = true)
    List<Event> findAllByInitiator(
            Long initiator,
            Long from,
            Long size
    );
}
